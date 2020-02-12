package lt.govilnius.domainService.schedule;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.domainService.mail.EmailSender;
import lt.govilnius.domainService.mail.EmailSenderConfig;
import lt.govilnius.domainService.mail.Mail;
import lt.govilnius.facadeService.reservation.InteractionWithMeetService;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.MeetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Optional;
import java.util.Set;

@Service
public class ProactiveMailSendingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionWithMeetService.class);

    @Autowired
    private MeetEngagementService meetEngagementService;

    @Autowired
    private MeetService meetService;

    @Autowired
    private EmailSender emailSender;

    @Value("${website.url}")
    private String websiteUrl;

    public Optional<Meet> processTouristRequest(Volunteer volunteer, Meet meet) {
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
            EmailSenderConfig emailSenderConfig = EmailSenderConfig.TOURIST_INFORMATION_CONFIG.apply(meet, volunteer, engagement);
            emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);

            LOGGER.info("Send information of the meet with id " + meet.getId() + " to volunteer with id " + volunteer.getId());
            emailSenderConfig = EmailSenderConfig.VOLUNTEER_INFORMATION_CONFIG.apply(meet, volunteer, engagement);
            emailSender.send(new Mail(volunteer.getEmail()), emailSenderConfig);

            meetEngagementService.getConfirmedByMeetId(meet.getId())
                    .stream().
                    filter(e -> !e.getVolunteer().getId().equals(volunteer.getId()))
                    .forEach(e -> {
                        final Volunteer v  = e.getVolunteer();
                        LOGGER.info("Send cancellation for the meet with id " + meet.getId() + " to volunteer with id " + v.getId());
                        emailSender.send(new Mail(v.getEmail()), EmailSenderConfig.VOLUNTEER_CANCELLATION_CONFIG.apply(meet, websiteUrl));
                    });
            return Optional.of(meet);
        } else {
            meet.setStatus(Status.CANCELED);
            meetService.edit(meet.getId(), meet);
            LOGGER.info("Send cancellation of the meet with id " + meet.getId() + " to tourist" );
            EmailSenderConfig emailSenderConfig = EmailSenderConfig.TOURIST_CANCELLATION_NOT_SELECTED_CONFIG.apply(meet, websiteUrl);
            emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);

            LOGGER.info("Send cancellation of the meet with id " + meet.getId() + " to volunteers ");
            meetEngagementService.getByMeetId(meet.getId()).forEach(e -> {
                final Volunteer v  = e.getVolunteer();
                LOGGER.info("Send cancellation for the meet with id " + meet.getId() + " to volunteer with id " + v.getId());
                emailSender.send(new Mail(v.getEmail()), emailSenderConfig);
            });
            return Optional.empty();
        }
    }

    public Optional<Meet> processAddition(Meet meet, Time time) {
        LOGGER.info("Process the additional information of the meet with id" + meet.getId());
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION);
        meet.setTime(time != null ? time : meet.getTime());
        meetService.edit(meet.getId(), meet).get();
        final Set<MeetEngagement> meetEngagements = meet.getMeetEngagements();
        meetEngagements.forEach(meetEngagement -> {
            final Volunteer volunteer = meetEngagement.getVolunteer();
            LOGGER.info("Send an additional request to volunteer with id " + volunteer.getId());
            emailSender.send(new Mail(volunteer.getEmail()),
                    EmailSenderConfig.VOLUNTEER_REQUEST_CONFIG.apply(meet, meetEngagement.getToken(), websiteUrl));
        });
        return Optional.of(meet);
    }
}
