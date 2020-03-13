package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.domainService.schedule.TouristMailProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class TouristActionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TouristActionService.class);

    @Autowired
    private MeetEngagementService meetEngagementService;

    @Autowired
    private MeetService meetService;

    @Autowired
    private TouristMailProcessor touristMailProcessor;

    @Autowired
    private MeetAgeGroupService meetAgeGroupService;

    @Autowired
    private EvaluationService evaluationService;

    @Value("${waiting.sent.volunteer.request.milliseconds}")
    private Long sentVolunteerRequestWaiting;

    @Value("${waiting.evaluation.milliseconds}")
    private Long evaluationWaiting;

    public Optional<Meet> editMeet(String meetId, List<AgeGroup> ageGroups) {
        LOGGER.info("Process change the meet with id " + meetId + " after addition is sent");
        try {
            return editMeet(Long.parseLong(meetId), ageGroups);
        } catch (RuntimeException e) {
            LOGGER.error("Fail to edit meet with id " + meetId, e);
            return Optional.empty();
        }
    }

    private Optional<Meet> editMeet(Long meetId, List<AgeGroup> ageGroups) {
        final Optional<Meet> meet = meetService.get(meetId);
        return meet
                .filter(e -> e.getStatus().equals(Status.SENT_TOURIST_ADDITION))
                .filter(e -> !e.getFreezed())
                .map(e -> {
                    for (MeetAgeGroup group : e.getMeetAgeGroups()) {
                        meetAgeGroupService.delete(group.getId());
                    }
                    e.setMeetAgeGroups(new HashSet<>());
                    for (AgeGroup group : ageGroups) {
                        meetAgeGroupService.create(e, group);
                    }
                    LOGGER.info("Process addition of the meet with id " + e.getId() + "after addition is sent");
                    e = meetService.setFreezed(e, true);
                    return touristMailProcessor.processAddition(e).orElse(null);
                });
    }

    public Optional<Meet> select(String token) {
        LOGGER.info("Process selection of the meet engagement with token " + token);
        final Optional<MeetEngagement> engagement = meetEngagementService.getByToken(token);
        return engagement
                .filter(e -> e.getMeet().getStatus().equals(Status.SENT_TOURIST_REQUEST))
                .filter(MeetEngagement::getConfirmed)
                .filter(e -> !e.getMeet().getFreezed())
                .map(e -> {
                    LOGGER.info("Process the meet engagement with token " + token);
                    e.setMeet(meetService.setFreezed(e.getMeet(), true));
                    return touristMailProcessor.processRequest(e.getVolunteer(), e.getMeet()).orElse(null);
                });
    }

    public Optional<Evaluation> evaluate(String token, String comment) {
        final Optional<MeetEngagement> engagement = meetEngagementService.getByToken(token);
        return engagement
                .filter(e -> e.getMeet().getStatus().equals(Status.FINISHED))
                .filter(e -> System.currentTimeMillis() - e.getMeet().getChangedAt().getTime() < evaluationWaiting)
                .filter(e -> !e.getMeet().getFreezed())
                .map(e -> {
                    LOGGER.info("Evaluate the meet engagement with token " + e.getToken());
                    meetService.setFreezed(e.getMeet(), true);
                    return evaluationService.create(e, comment, UserType.TOURIST).orElse(null);
                });
    }
}