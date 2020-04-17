package lt.govilnius.domainService.schedule;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.domainService.mail.EmailSender;
import lt.govilnius.domainService.mail.EmailSenderConfig;
import lt.govilnius.domainService.mail.Mail;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.MeetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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

    @Value("${registration.url}")
    private String registrationUrl;

    @Value("${waiting.sent.tourist.request.milliseconds}")
    private Long sentTouristRequestWaiting;

    @Value("${waiting.addition.milliseconds}")
    private Long additionWaiting;

    public void processRequests() {
        meetService.findByStatus(Status.SENT_TOURIST_REQUEST).forEach(meet -> {
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= sentTouristRequestWaiting) {
                sendCancellations(meet);
            }
        });
    }

    public void processAdditions() {
        meetService.findByStatus(Status.SENT_TOURIST_ADDITION).forEach(meet -> {
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= additionWaiting) {
                sendCancellations(meet);
            }
        });
    }

    public Optional<Meet> processRequest(Volunteer volunteer, Meet meet) {
        LOGGER.info("Process tourist request of the meet with id " + meet.getId());
        final Optional<MeetEngagement> meetEngagement = volunteer != null ?
                meetEngagementService.findByMeetIdAndVolunteerId(meet.getId(), volunteer.getId()) :
                Optional.empty();
        if (meetEngagement.isPresent()) {
            meet.setVolunteer(volunteer);
            meet.setStatus(Status.AGREED);
            meetService.edit(meet.getId(), meet);
            MeetEngagement engagement = meetEngagement.get();
            engagement = meetEngagementService.setFreezed(engagement, false);
            LOGGER.info("Send information of the meet with id " + meet.getId() + " to tourist");
            EmailSenderConfig emailSenderConfig = EmailSenderConfig.TOURIST_INFORMATION_CONFIG.apply(meet, volunteer, engagement);
            meet = meetService.setFreezed(meet, false);
            emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);

            LOGGER.info("Send information of the meet with id " + meet.getId() + " to volunteer with id " + volunteer.getId());
            emailSenderConfig = EmailSenderConfig.VOLUNTEER_INFORMATION_CONFIG.apply(meet, volunteer, engagement);
            emailSender.send(new Mail(volunteer.getEmail()), emailSenderConfig);

            Meet finalMeet = meet;
            meetEngagementService.getConfirmedByMeetId(meet.getId())
                    .stream().
                    filter(e -> !e.getVolunteer().getId().equals(volunteer.getId()))
                    .forEach(e -> {
                        final Volunteer v  = e.getVolunteer();
                        LOGGER.info("Send cancellation for the meet with id " + finalMeet.getId() + " to volunteer with id " + v.getId());
                        emailSender.send(new Mail(v.getEmail()), EmailSenderConfig.VOLUNTEER_CANCELLATION_CONFIG.apply(finalMeet, websiteUrl));
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
        EmailSenderConfig emailSenderConfig = EmailSenderConfig.TOURIST_CANCELLATION_NOT_SELECTED_CONFIG.apply(meet, registrationUrl);
        emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);

        LOGGER.info("Send cancellation of the meet with id " + meet.getId() + " to volunteers ");
        meetEngagementService.getByMeetId(meet.getId()).forEach(e -> {
            EmailSenderConfig config = EmailSenderConfig.VOLUNTEER_CANCELLATION_CONFIG.apply(meet, websiteUrl);
            final Volunteer v  = e.getVolunteer();
            LOGGER.info("Send cancellation for the meet with id " + meet.getId() + " to volunteer with id " + v.getId());
            emailSender.send(new Mail(v.getEmail()), config);
        });
    }

    public Optional<Meet> processAddition(Meet meet) {
        LOGGER.info("Process the additional information of the meet with id" + meet.getId());
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION);
        meet.setFreezed(false);
        meetService.edit(meet.getId(), meet);
        final Set<MeetEngagement> meetEngagements = meet.getMeetEngagements();
        meetEngagements.forEach(meetEngagement -> {
            final Volunteer volunteer = meetEngagement.getVolunteer();
            LOGGER.info("Send an additional request to volunteer with id " + volunteer.getId());
            meetEngagement = meetEngagementService.setFreezed(meetEngagement, false);
            emailSender.send(new Mail(volunteer.getEmail()),
                    EmailSenderConfig.VOLUNTEER_REQUEST_CONFIG.apply(meet, meetEngagement.getToken(), websiteUrl));
        });
        return Optional.of(meet);
    }
}
