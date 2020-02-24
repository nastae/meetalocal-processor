package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.Evaluation;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.domain.reservation.UserType;
import lt.govilnius.domainService.schedule.TouristMailProcessor;
import lt.govilnius.domainService.time.TimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.text.ParseException;
import java.util.Optional;

@Component
public class VolunteerActionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VolunteerActionService.class);

    @Autowired
    private MeetEngagementService meetEngagementService;

    @Autowired
    private MeetService meetService;

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private TouristMailProcessor touristMailProcessor;

    @Value("${waiting.sent.volunteer.request.milliseconds}")
    private Long sentVolunteerRequestWaiting;

    @Value("${waiting.evaluation.milliseconds}")
    private Long evaluationWaiting;

    public Optional<MeetEngagement> agree(String token) {
        LOGGER.info("Process agreement of the meet engagement with token " + token);
        final Optional<MeetEngagement> engagement = meetEngagementService.getByToken(token);
        return engagement
                .filter(e -> e.getMeet().getStatus().equals(Status.SENT_VOLUNTEER_REQUEST))
                .filter(e -> System.currentTimeMillis() - e.getMeet().getChangedAt().getTime() < sentVolunteerRequestWaiting)
                .filter(e -> !e.getMeet().getFreezed())
                .map(e -> {
                    LOGGER.info("Edit the meet engagement with token " + e.getToken());
                    e.setConfirmed(true);
                    e.setMeet(meetService.setFreezed(e.getMeet(), true));
                    return meetEngagementService.edit(e.getId(), e).orElse(null);
                });
    }

    public Optional<MeetEngagement> cancel(String token) {
        LOGGER.info("Process cancellation of the meet engagement with token " + token);
        final Optional<MeetEngagement> engagement = meetEngagementService.getByToken(token);
        return engagement
                .filter(e -> e.getMeet().getStatus().equals(Status.SENT_VOLUNTEER_REQUEST))
                .filter(e -> System.currentTimeMillis() - e.getMeet().getChangedAt().getTime() < sentVolunteerRequestWaiting)
                .filter(e -> !e.getMeet().getFreezed())
                .map(e -> {
                    LOGGER.info("Edit the meet engagement with token " + e.getToken());
                    e.setConfirmed(false);
                    e.setMeet(meetService.setFreezed(e.getMeet(), true));
                    return meetEngagementService.edit(e.getId(), e).orElse(null);
                });
    }

    public Optional<MeetEngagement> editEngagement(String token, String timeString) {
        LOGGER.info("Process change the meet engagement with token " + token);
        final Time time;
        try {
            time = TimeConverter.convertToTime(timeString);
            return editEngagement(token, time);
        } catch (ParseException e) {
            LOGGER.error("Fail to parse time " + timeString + " of the meet engagement with token " + token, e);
            return Optional.empty();
        }
    }

    private Optional<MeetEngagement> editEngagement(String token, Time time) {
        final Optional<MeetEngagement> engagement = meetEngagementService.getByToken(token);
        return engagement
                .filter(e -> e.getMeet().getStatus().equals(Status.SENT_VOLUNTEER_REQUEST))
                .filter(e -> System.currentTimeMillis() - e.getMeet().getChangedAt().getTime() < sentVolunteerRequestWaiting)
                .filter(e -> !e.getMeet().getFreezed())
                .map(e -> {
                    LOGGER.info("Edit the meet engagement with token " + e.getToken());
                    e.setConfirmed(true);
                    e.setTime(time != null ? time : e.getTime());
                    e.setMeet(meetService.setFreezed(e.getMeet(), true));
                    return meetEngagementService.edit(e.getId(), e).orElse(null);
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
                    return evaluationService.create(e, comment, UserType.VOLUNTEER).orElse(null);
                });
    }
}
