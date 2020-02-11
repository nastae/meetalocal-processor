package lt.govilnius.domainService.schedule;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.domainService.filter.MeetEngagementFilter;
import lt.govilnius.domainService.filter.VolunteerFilter;
import lt.govilnius.domainService.mail.EmailSender;
import lt.govilnius.domainService.mail.EmailSenderConfig;
import lt.govilnius.domainService.mail.Mail;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.MeetService;
import lt.govilnius.facadeService.reservation.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public class MailSendingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSendingService.class);

    @Autowired
    private MeetService meetService;

    @Autowired
    private MeetEngagementService meetEngagementService;

    @Autowired
    private MeetEngagementFilter meetEngagementFilter;

    @Autowired
    private ReportService reportService;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private VolunteerFilter volunteerFilter;

    @Value("${website.url}")
    private String websiteUrl;

    @Value("${waiting.sent.request.milliseconds}")
    private Long sentRequestWaiting;

    @Value("${waiting.additional.milliseconds}")
    private Long additionalWaiting;

    @Value("${waiting.responses.milliseconds}")
    private Long responsesWaiting;

    @Value("${waiting.evaluation.milliseconds}")
    private Long evaluationWaiting;

    public void processNews() {
        meetService.findByStatus(Status.NEW).forEach(meet -> {
            LOGGER.info("Process the new meet with id " + meet.getId());
            final List<MeetEngagement> engagements = createEngagements(volunteerFilter.filterByMeet(meet), meet);
            if (engagements.size() > 0) {
                meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
                meetService.edit(meet.getId(), meet);
                engagements.forEach(engagement -> {
                    LOGGER.info("Created a meet engagement of the meet with id " + meet.getId());
                    final String token = engagement.getToken();
                    emailSender.send(new Mail(engagement.getVolunteer().getEmail()), EmailSenderConfig.VOLUNTEER_REQUEST_CONFIG.apply(
                            meet, token, websiteUrl));
                });
            } else {
                emailSender.send(new Mail(meet.getEmail()), prepareTouristCanceledConfig(meet));
            }
        });
    }

    private List<MeetEngagement> createEngagements(List<Volunteer> volunteers, Meet meet) {
        return volunteers.stream()
                .map(volunteer -> meetEngagementService.create(meet, volunteer, meet.getTime()))
                .flatMap(o -> o.map(Stream::of).orElseGet(Stream::empty))
                .collect(toList());
    }

    public void processVolunteerRequests() {
        meetService.findByStatus(Status.SENT_VOLUNTEER_REQUEST).forEach(meet -> {
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= sentRequestWaiting) {
                LOGGER.info("Process the sent request to meet whose id " + meet.getId());
                final List<MeetEngagement> engagements = meetEngagementFilter.filterForMail(
                        getConfirmedEngagements(meet.getId()), meet);;
                final List<Report> reports = reportService.getByMeetId(meet.getId());
                EmailSenderConfig emailSenderConfig = null;
                if (engagements.size() > 0) {
                    emailSenderConfig = prepareSentTouristRequestConfig(meet, engagements);
                } else if (reports.size() > 0) {
                    emailSenderConfig = prepareTouristReportConfig(meet);
                } else {
                    emailSenderConfig = prepareTouristAdditionConfig(meet);
                }
                emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);
            }
        });
    }

    private List<MeetEngagement> getConfirmedEngagements(Long meetId) {
        return meetEngagementService.getByMeetId(meetId)
                .stream()
                .filter(MeetEngagement::getConfirmed)
                .collect(toList());
    }

    public void processAdditions() {
        meetService.findByStatus(Status.SENT_TOURIST_ADDITION).forEach(meet -> {
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= additionalWaiting) {
                LOGGER.info("Process the additional information of the meet with id" + meet.getId());
                meet.setStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION);
                meetService.edit(meet.getId(), meet);
                final Set<MeetEngagement> meetEngagements = meet.getMeetEngagements();
                meetEngagements.forEach(meetEngagement -> {
                    final Volunteer volunteer = meetEngagement.getVolunteer();
                    LOGGER.info("Send an additional request to volunteer with id " + volunteer.getId());
                    emailSender.send(new Mail(volunteer.getEmail()),
                            EmailSenderConfig.VOLUNTEER_REQUEST_CONFIG.apply(meet, meetEngagement.getToken(), websiteUrl));
                });
            }
        });
    }

    public void processRequestsAfterAddition() {
        meetService.findByStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION).forEach(meet -> {
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= sentRequestWaiting) {
                LOGGER.info("Process the sent request of the meet with id " + meet.getId());
                List<MeetEngagement> meetEngagements = getConfirmedEngagements(meet.getId());
                List<Report> reports = reportService.getByMeetId(meet.getId());
                EmailSenderConfig emailSenderConfig = null;
                if (meetEngagements.size() > 0) {
                    emailSenderConfig = prepareSentTouristRequestConfig(meet, meetEngagements);
                } else if (reports.size() > 0) {
                    emailSenderConfig = prepareTouristReportConfig(meet);
                } else {
                    emailSenderConfig = prepareTouristCanceledConfig(meet);
                }
                emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);
            }
        });
    }

    public void processTouristRequests() {
        meetService.findByStatus(Status.SENT_TOURIST_REQUEST).forEach(meet -> {
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= responsesWaiting) {
                LOGGER.info("Process tourist request of the meet with id " + meet.getId());
                final Volunteer volunteer = meet.getVolunteer();
                final Optional<MeetEngagement> meetEngagement = volunteer != null ?
                        meetEngagementService.findByMeetIdAndVolunteerId(meet.getId(), volunteer.getId()) :
                        Optional.empty();
                if (meetEngagement.isPresent()) {
                    meet.setStatus(Status.AGREED);
                    meetService.edit(meet.getId(), meet);
                    final MeetEngagement engagement = meetEngagement.get();
                    LOGGER.info("Send information of the meet with id " + meet.getId() + " to tourist");
                    EmailSenderConfig emailSenderConfig = EmailSenderConfig.TOURIST_INFORMATION_CONFIG.apply(meet, volunteer, engagement);
                    emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);

                    LOGGER.info("Send information of the meet with id " + meet.getId() + " to volunteer with id " + volunteer.getId());
                    emailSenderConfig = EmailSenderConfig.VOLUNTEER_INFORMATION_CONFIG.apply(meet, volunteer, engagement);
                    emailSender.send(new Mail(volunteer.getEmail()), emailSenderConfig);

                    getConfirmedEngagements(meet.getId())
                            .stream().
                            filter(e -> !e.getVolunteer().getId().equals(volunteer.getId()))
                            .forEach(e -> {
                                final Volunteer v  = e.getVolunteer();
                                LOGGER.info("Send cancellation for the meet with id " + meet.getId() + " to volunteer with id " + v.getId());
                                emailSender.send(new Mail(v.getEmail()), EmailSenderConfig.VOLUNTEER_CANCELLATION_CONFIG.apply(meet, websiteUrl));
                            });
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
                }
            }
        });
    }

    public void processAgreements() {
        meetService.findByStatus(Status.AGREED).forEach(meet -> {
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= evaluationWaiting) {
                LOGGER.info("Process agreement of the meet with id " + meet.getId());
                final Volunteer volunteer = meet.getVolunteer();
                if (volunteer != null) {
                    LOGGER.info("Send evaluation form of the meet with id " + meet.getId() + " to tourist");
                    EmailSenderConfig emailSenderConfig = EmailSenderConfig.TOURIST_EVALUATION_CONFIG.apply(meet, websiteUrl);
                    emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);

                    LOGGER.info("Send evaluation form of the meet with id " + meet.getId() + " to volunteer with id " + volunteer.getId());
                    emailSenderConfig = EmailSenderConfig.VOLUNTEER_EVALUATION_CONFIG.apply(meet, websiteUrl);
                    emailSender.send(new Mail(volunteer.getEmail()), emailSenderConfig);

                    meet.setStatus(Status.FINISHED);
                    meetService.edit(meet.getId(), meet);
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

    private EmailSenderConfig prepareTouristReportConfig(Meet meet) {
        LOGGER.info("Send report of the meet with id " + meet.getId() + " to tourist");
        meet.setStatus(Status.REPORTED);
        meetService.edit(meet.getId(), meet);
        return EmailSenderConfig.TOURIST_CANCELLATION_CONFIG.apply(meet, websiteUrl);
    }

    private EmailSenderConfig prepareTouristCanceledConfig(Meet meet) {
        LOGGER.info("Send cancellation of the meet with id " + meet.getId() + " to tourist");
        meet.setStatus(Status.CANCELED);
        meetService.edit(meet.getId(), meet);
        return EmailSenderConfig.TOURIST_CANCELLATION_CONFIG.apply(meet, websiteUrl);
    }

    private EmailSenderConfig prepareTouristAdditionConfig(Meet meet) {
        LOGGER.info("Send addition of the meet with id " + meet.getId() + " to tourist");
        meet.setStatus(Status.SENT_TOURIST_ADDITION);
        meetService.edit(meet.getId(), meet);
        return EmailSenderConfig.TOURIST_ADDITION_CONFIG.apply(meet, websiteUrl);
    }
}
