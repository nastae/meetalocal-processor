package lt.govilnius.domainService.schedule;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.domainService.filter.MeetEngagementFilter;
import lt.govilnius.domainService.filter.VolunteerFilter;
import lt.govilnius.domainService.mail.EmailSender;
import lt.govilnius.domainService.mail.EmailSenderConfig;
import lt.govilnius.domainService.mail.Mail;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.MeetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class VolunteerMailProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(VolunteerMailProcessor.class);

    @Autowired
    private MeetService meetService;

    @Autowired
    private MeetEngagementService meetEngagementService;

    @Autowired
    private MeetEngagementFilter meetEngagementFilter;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private VolunteerFilter volunteerFilter;

    @Value("${website.url}")
    private String websiteUrl;

    @Value("${registration.url}")
    private String registrationUrl;

    @Value("${waiting.sent.volunteer.request.milliseconds}")
    private Long sentVolunteerRequestWaiting;

    @Value("${waiting.evaluation.milliseconds}")
    private Long evaluationWaiting;

    @Value("${mail.accepting.start.hours}")
    private Long mailAcceptingStartHours;

    @Value("${mail.accepting.end.hours}")
    private Long mailAcceptingEndHours;

    public void processNews() {
        meetService.findByStatus(Status.NEW).forEach(meet -> {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(meet.getCreatedAt().getTime());
            Calendar currentCal = Calendar.getInstance();
            currentCal.setTimeInMillis(System.currentTimeMillis());
            if ((cal.getTime().getHours() < mailAcceptingEndHours && cal.getTime().getHours() > mailAcceptingStartHours) ||
                    ((cal.getTime().getHours() >= mailAcceptingEndHours && cal.getTime().getHours() <= mailAcceptingStartHours) &&
                            (currentCal.getTime().getHours() < mailAcceptingEndHours && currentCal.getTime().getHours() > mailAcceptingStartHours))) {
                LOGGER.info("Process the new meet with id " + meet.getId());
                final List<MeetEngagement> engagements = createEngagements(volunteerFilter.filterByMeet(meet), meet);
                if (engagements.size() > 0) {
                    meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
                    meetService.edit(meet.getId(), meet);
                    engagements.forEach(engagement -> {
                        engagement = meetEngagementService.setFreezed(engagement, false);
                        LOGGER.info("Created a meet engagement of the meet with id " + meet.getId());
                        final String token = engagement.getToken();
                        emailSender.send(new Mail(engagement.getVolunteer().getEmail()), EmailSenderConfig.VOLUNTEER_REQUEST_CONFIG.apply(
                                meet, token, websiteUrl));
                    });
                } else {
                    emailSender.send(new Mail(meet.getEmail()), prepareTouristCanceledConfig(meet));
                }
            }
        });
    }

    private List<MeetEngagement> createEngagements(List<Volunteer> volunteers, Meet meet) {
        return volunteers.stream()
                .map(volunteer -> meetEngagementService.create(meet, volunteer, meet.getTime()))
                .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .limit(10)
                .collect(toList());
    }

    public void processRequests() {
        meetService.findByStatus(Status.SENT_VOLUNTEER_REQUEST).forEach(meet -> {
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= sentVolunteerRequestWaiting) {
                LOGGER.info("Process the sent request to meet whose id " + meet.getId());
                meet = meetService.setFreezed(meet, false);
                List<MeetEngagement> engagements = meetEngagementFilter.filterForMail(
                        meetEngagementService.getConfirmedByMeetId(meet.getId()), meet);
                EmailSenderConfig emailSenderConfig = null;
                if (engagements.size() > 0) {
                    emailSenderConfig = prepareSentTouristRequestConfig(meet, engagements.stream().limit(5).collect(toList()));
                    for (int i = 5; i < engagements.size(); i++) {
                        MeetEngagement mEngagement = engagements.get(i);
                        LOGGER.info("Send cancellation form of the meet with id " + mEngagement.getMeet().getId() +
                                " to volunteer with id " + mEngagement.getVolunteer().getId());
                        EmailSenderConfig config = EmailSenderConfig.VOLUNTEER_CANCELLATION_CONFIG.apply(mEngagement.getMeet(), websiteUrl);
                        emailSender.send(new Mail(mEngagement.getVolunteer().getEmail()), config);
                    }
                } else {
                    emailSenderConfig = prepareTouristCanceledConfig(meet);
                }
                engagements = meetEngagementService.getNotConfirmedByMeetId(meet.getId());
                for (MeetEngagement mEngagement : engagements) {
                    LOGGER.info("Send cancellation form of the meet with id " + mEngagement.getMeet().getId() +
                            " to volunteer with id " + mEngagement.getVolunteer().getId());
                    EmailSenderConfig config = EmailSenderConfig.VOLUNTEER_CANCELLATION_CONFIG.apply(mEngagement.getMeet(), websiteUrl);
                    emailSender.send(new Mail(mEngagement.getVolunteer().getEmail()), config);
                }
                emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);
            }
        });
    }

    public void processAgreements() {
        meetService.findByStatus(Status.AGREED).forEach(meet -> {
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= evaluationWaiting) {
                LOGGER.info("Process agreement of the meet with id " + meet.getId());
                meet = meetService.setFreezed(meet, false);
                final Volunteer volunteer = meet.getVolunteer();
                if (volunteer != null) {
                    Optional<MeetEngagement> engagementOptional = meetEngagementService.findByMeetIdAndVolunteerId(meet.getId(), volunteer.getId());
                    if (engagementOptional.isPresent()) {
                        MeetEngagement engagement = engagementOptional.get();
                        engagement = meetEngagementService.setFreezed(engagement, false);
                        LOGGER.info("Send evaluation form of the meet with id " + meet.getId() + " to tourist");
                        EmailSenderConfig emailSenderConfig = EmailSenderConfig.TOURIST_EVALUATION_CONFIG.apply(engagement, websiteUrl);
                        emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);

                        LOGGER.info("Send evaluation form of the meet with id " + meet.getId() + " to volunteer with id " + volunteer.getId());
                        emailSenderConfig = EmailSenderConfig.VOLUNTEER_EVALUATION_CONFIG.apply(engagement, websiteUrl);
                        emailSender.send(new Mail(volunteer.getEmail()), emailSenderConfig);

                        meet.setStatus(Status.FINISHED);
                        meetService.edit(meet.getId(), meet);
                    } else {
                        LOGGER.error("Fail to send evaluations of the meet with id " + meet.getId());
                        meet.setStatus(Status.ERROR);
                        meetService.edit(meet.getId(), meet);
                    }
                } else {
                    LOGGER.error("Fail to send evaluations of the meet with id " + meet.getId());
                    meet.setStatus(Status.ERROR);
                    meetService.edit(meet.getId(), meet);
                }
            }
        });
    }

    private EmailSenderConfig prepareSentTouristRequestConfig(Meet meet, List<MeetEngagement> meetEngagements) {
        LOGGER.info("Send volunteers requests of the meet with id " + meet.getId() + " to tourist");
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meetService.edit(meet.getId(), meet);
        return EmailSenderConfig.TOURIST_REQUEST_CONFIG.apply(meet, meetEngagements, websiteUrl);
    }


    private EmailSenderConfig prepareTouristCanceledConfig(Meet meet) {
        LOGGER.info("Send cancellation of the meet with id " + meet.getId() + " to tourist");
        meet.setStatus(Status.CANCELED);
        meetService.edit(meet.getId(), meet);
        return EmailSenderConfig.TOURIST_CANCELLATION_CONFIG.apply(meet, registrationUrl);
    }
}
