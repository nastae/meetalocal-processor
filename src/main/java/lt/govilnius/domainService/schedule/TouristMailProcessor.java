package lt.govilnius.domainService.schedule;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.domainService.mail.*;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.MeetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class TouristMailProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TouristMailProcessor.class);

    @Autowired
    private MeetEngagementService meetEngagementService;

    @Autowired
    private MeetService meetService;

    @Autowired
    private EmailSender emailSender;

    @Value("${website.url}")
    private String websiteUrl;

    @Value("${live.registration.url}")
    private String liveRegistrationUrl;

    @Value("${online.registration.url}")
    private String onlineRegistrationUrl;

    @Value("${waiting.sent.tourist.request.milliseconds}")
    private Long sentTouristRequestWaiting;

    @Autowired
    private EmailSenderConfigFactory emailSenderConfigFactory;

    @Transactional
    public void processRequests() {
        meetService.findByStatus(Status.SENT_LOCAL_REQUEST).forEach(meet -> {
            LOGGER.info("Try process tourist request of the meet with id " + meet.getId());
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= sentTouristRequestWaiting) {
                sendCancellations(meet);
            }
        });
    }

    @Transactional
    public Optional<Meet> processRequest(Volunteer volunteer, Meet meet) {
        LOGGER.info("Process tourist request of the meet with id " + meet.getId());
        final Optional<MeetEngagement> meetEngagement = volunteer != null ?
                meetEngagementService.findByMeetIdAndVolunteerId(meet.getId(), volunteer.getId()) :
                Optional.empty();
        if (meetEngagement.isPresent()) {
            meet.setVolunteer(volunteer);
            meet.setStatus(Status.AGREED);
            meetService.edit(meet.getId(), meet);
            final MeetEngagement engagement = meetEngagement.get();
            LOGGER.info("Send information of the meet with id " + meet.getId() + " to tourist");
            EmailSenderConfig emailSenderConfig = emailSenderConfigFactory.getLocalInformationConfig(meet, volunteer, engagement);
            emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);

            LOGGER.info("Send information of the meet with id " + meet.getId() + " to volunteer with id " + volunteer.getId());
            emailSenderConfig = emailSenderConfigFactory.getVolunteerInformationConfig(meet, volunteer, engagement);
            emailSender.send(new Mail(volunteer.getEmail()), emailSenderConfig);

            meetEngagementService.getConfirmedByMeetId(meet.getId())
                    .stream().
                    filter(e -> !e.getVolunteer().getId().equals(volunteer.getId()))
                    .forEach(e -> {
                        final Volunteer v  = e.getVolunteer();
                        LOGGER.info("Send cancellation for the meet with id " + meet.getId() + " to volunteer with id " + v.getId());
                        emailSender.send(new Mail(v.getEmail()), emailSenderConfigFactory.getVolunteerCancellationConfig(meet, websiteUrl));
                    });
            return Optional.of(meet);
        } else {
            sendCancellations(meet);
            return Optional.empty();
        }
    }

    private void sendCancellations(Meet meet) {
        meet.setStatus(Status.CANCELED);
        meetService.edit(meet.getId(), meet);
        LOGGER.info("Send cancellation of the meet with id " + meet.getId() + " to tourist" );
        String registrationUrl = meet.getType().equals(MeetType.Name.LIVE) ? liveRegistrationUrl : onlineRegistrationUrl;
        EmailSenderConfig emailSenderConfig = emailSenderConfigFactory.getLocalCancellationNotSelectedConfig(meet, registrationUrl);
        emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);

        LOGGER.info("Send cancellation of the meet with id " + meet.getId() + " to volunteers ");
        meetEngagementService.getByMeetId(meet.getId()).forEach(e -> {
            EmailSenderConfig config = emailSenderConfigFactory.getVolunteerCancellationConfig(meet, websiteUrl);
            final Volunteer v  = e.getVolunteer();
            LOGGER.info("Send cancellation for the meet with id " + meet.getId() + " to volunteer with id " + v.getId());
            emailSender.send(new Mail(v.getEmail()), config);
        });
    }
}
