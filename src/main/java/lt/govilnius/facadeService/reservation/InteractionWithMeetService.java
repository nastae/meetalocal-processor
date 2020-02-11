package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Report;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.domainService.time.TimeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.util.Optional;

@Service
public class InteractionWithMeetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionWithMeetService.class);

    @Autowired
    private MeetEngagementService meetEngagementService;

    @Autowired
    private MeetService meetService;

    @Autowired
    private ReportService reportService;

    @Value("${waiting.sent.request.milliseconds}")
    private Long sentRequestWaiting;

    @Value("${waiting.additional.milliseconds}")
    private Long additionalWaiting;

    @Value("${waiting.responses.milliseconds}")
    private Long responsesWaiting;

    public Optional<MeetEngagement> agree(String token) {
        LOGGER.info("Process agreement of the meet engagement with token " + token);
        return changeEngage(token, true, null);
    }

    public Optional<MeetEngagement> cancel(String token) {
        LOGGER.info("Process cancellation of the meet engagement with token " + token);
        return changeEngage(token, false, null);
    }

    public Optional<MeetEngagement> changeEngagement(String token, String timeString) {
        LOGGER.info("Process change the meet engagement with token " + token);
        final Time time;
        try {
            time = TimeConverter.convertToTime(timeString);
            return changeEngage(token, true, time);
        } catch (ParseException e) {
            LOGGER.error("Fail to parse time " + timeString + " of the meet engagement with token " + token, e);
            return Optional.empty();
        }
    }

    public Optional<Meet> changeMeetAfterAddition(String meetId, String timeString) {
        LOGGER.info("Process change the meet with id " + meetId + " after addition is sent");
        final Time time;
        try {
            time = TimeConverter.convertToTime(timeString);
            return changeEngageAfterAddition(Long.parseLong(meetId), time);
        } catch (ParseException e) {
            LOGGER.error("Fail to parse values changing meet with id " + meetId, e);
            return Optional.empty();
        }
    }

    public Optional<Report> report(String token, String comment) {
        LOGGER.info("Process report of the meet engagement with token " + token);
        final Optional<MeetEngagement> engagement = meetEngagementService.getByToken(token);
        return engagement
                .filter(e -> e.getMeet().getStatus().equals(Status.SENT_VOLUNTEER_REQUEST))
                .filter(e -> System.currentTimeMillis() - e.getMeet().getChangedAt().getTime() < sentRequestWaiting)
                .map(e -> {
                    LOGGER.info("Report the meet engagement with token " + e.getToken());
                    return reportService.create(e.getMeet(), e.getVolunteer(), comment).orElse(null);
                });
    }

    public Optional<Meet> select(String token) {
        LOGGER.info("Process selection of the meet engagement with token " + token);
        final Optional<MeetEngagement> engagement = meetEngagementService.getByToken(token);
        return engagement
                .filter(e -> e.getMeet().getStatus().equals(Status.SENT_TOURIST_REQUEST))
                .filter(e -> System.currentTimeMillis() - e.getMeet().getChangedAt().getTime() < responsesWaiting)
                .filter(MeetEngagement::getConfirmed)
                .map(e -> {
                    LOGGER.info("Edit the meet engagement with token " + e.getToken());
                    Meet meet = e.getMeet();
                    meet.setVolunteer(e.getVolunteer());
                    return meetService.edit(meet.getId(), meet).orElse(null);
                });
    }

    private Optional<MeetEngagement> changeEngage(String token, boolean engage, Time time) {
        final Optional<MeetEngagement> engagement = meetEngagementService.getByToken(token);
        return engagement
                .filter(e -> e.getMeet().getStatus().equals(Status.SENT_VOLUNTEER_REQUEST))
                .filter(e -> System.currentTimeMillis() - e.getMeet().getChangedAt().getTime() < sentRequestWaiting)
                .map(e -> {
                    LOGGER.info("Edit the meet engagement with token " + e.getToken());
                    e.setConfirmed(engage);
                    e.setTime(time != null ? time : e.getTime());
                    return meetEngagementService.edit(e.getId(), e).orElse(null);
                });
    }

    private Optional<Meet> changeEngageAfterAddition(Long meetId, Time time) {
        final Optional<Meet> meet = meetService.get(meetId);
        return meet
                .filter(e -> e.getStatus().equals(Status.SENT_TOURIST_ADDITION))
                .filter(e -> System.currentTimeMillis() - e.getChangedAt().getTime() < additionalWaiting)
                .map(e -> {
                    LOGGER.info("Edit the meet with id " + e.getId() + "after addition is sent");
                    e.setTime(time != null ? time : e.getTime());
                    return meetService.edit(e.getId(), e).orElse(null);
                });
    }
}