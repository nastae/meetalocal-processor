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

import javax.transaction.Transactional;
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

    @Value("${max.meet.waiting.hours}")
    private Long maxMeetWaitingHours;

    @Transactional
    public void processNews() {
        meetService.findByStatus(Status.NEW).forEach(meet -> {
            LOGGER.info("Try process the new meet with id " + meet.getId());
            if (isValidMeeting(meet)) {
                emailSender.send(new Mail(meet.getEmail()), prepareTouristCanceledMeetDateNotValidConfig(meet));
            } else if (!isNightTime(meet)) {
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

    private boolean isNightTime(Meet meet) {
        final Calendar formCreated = Calendar.getInstance();
        formCreated.setTimeInMillis(meet.getCreatedAt().getTime());
        final Calendar current = Calendar.getInstance();
        current.setTimeInMillis(System.currentTimeMillis());

        boolean isNight = (mailAcceptingEndHours < formCreated.get(Calendar.HOUR_OF_DAY) || formCreated.get(Calendar.HOUR_OF_DAY) < mailAcceptingStartHours) &&
                (mailAcceptingEndHours < current.get(Calendar.HOUR_OF_DAY) || current.get(Calendar.HOUR_OF_DAY) < mailAcceptingStartHours);
        LOGGER.info("Checking if meeting time is night time!");
        LOGGER.info("Is night time? " + isNight);
        LOGGER.info("Form create at " + formCreated.toString() + ", current time is " + current.toString());
        return isNight;
    }

    private boolean isValidMeeting(Meet meet) {
        final Calendar availableMeeting = Calendar.getInstance();
        availableMeeting.setTimeInMillis(System.currentTimeMillis() + maxMeetWaitingHours * 3600000 - (5 * 60 * 1000));
        final Calendar meeting = Calendar.getInstance();
        meeting.set(meet.getDate().getYear(), meet.getDate().getMonth(), meet.getDate().getDay(),
                meet.getTime().getHours(), meet.getTime().getMinutes(), meet.getTime().getSeconds());
        LOGGER.info("Checking if meeting time is valid!");
        boolean isValid = availableMeeting.before(meeting);
        LOGGER.info("Is valid time? " + isValid);
        LOGGER.info("Available meeting time is " + availableMeeting.toString() + ", meeting time is " + meeting.toString());
        return isValid;
    }

    private List<MeetEngagement> createEngagements(List<Volunteer> volunteers, Meet meet) {
        return volunteers.stream()
                .map(volunteer -> meetEngagementService.create(meet, volunteer, meet.getTime()))
                .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .limit(10)
                .collect(toList());
    }

    @Transactional
    public void processRequests() {
        meetService.findByStatus(Status.SENT_VOLUNTEER_REQUEST).forEach(meet -> {
            LOGGER.info("Try process the sent request to meet whose id " + meet.getId());
            List<MeetEngagement> meetEngagements = meetEngagementService.getByMeetId(meet.getId());
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= sentVolunteerRequestWaiting ||
                    meetEngagements.stream().allMatch(MeetEngagement::getConfirmed)) {
                List<MeetEngagement> engagements = meetEngagementFilter.filterForMail(
                        meetEngagementService.getConfirmedByMeetId(meet.getId()), meet);
                LOGGER.info("Process the sent request to meet whose id " + meet.getId());
                meet = meetService.setFreezed(meet, false);
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

    @Transactional
    public void processAgreements() {
        meetService.findByStatus(Status.AGREED).forEach(meet -> {
            LOGGER.info("Try process agreement of the meet with id " + meet.getId());
            Calendar date = Calendar.getInstance();
            date.setTime(meet.getDate());
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(meet.getTime().getTime());
            Calendar cal = Calendar.getInstance();
            cal.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE),
                    time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.SECOND));
            if (System.currentTimeMillis() - cal.getTimeInMillis() >= evaluationWaiting) {
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
        meet.setFreezed(true);
        meetService.edit(meet.getId(), meet);
        return EmailSenderConfig.TOURIST_CANCELLATION_CONFIG.apply(meet, registrationUrl);
    }

    private EmailSenderConfig prepareTouristCanceledMeetDateNotValidConfig(Meet meet) {
        LOGGER.info("Send cancellation of the meet with id " + meet.getId() + " to tourist");
        meet.setStatus(Status.CANCELED);
        meet.setFreezed(true);
        meetService.edit(meet.getId(), meet);
        return EmailSenderConfig.TOURIST_CANCELLATION_NOT_VALID_MEET_DATE_CONFIG.apply(meet, registrationUrl);
    }
}
