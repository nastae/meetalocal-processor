package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.domainService.schedule.TouristMailProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
                .filter(e -> !e.getMeet().getFreezed())
                .map(e -> {
                    LOGGER.info("Evaluate the meet engagement with token " + e.getToken());
                    meetService.setFreezed(e.getMeet(), true);
                    return evaluationService.create(e, comment, UserType.TOURIST).orElse(null);
                });
    }

    public boolean isFreezed(String token) {
        final Optional<MeetEngagement> engagement = meetEngagementService.getByToken(token);
        return engagement.isPresent() ? engagement.get().getMeet().getFreezed() : false;
    }

    public boolean isFreezed(Optional<Meet> meet) {
        return meet.isPresent() ? meet.get().getFreezed() : false;
    }
}
