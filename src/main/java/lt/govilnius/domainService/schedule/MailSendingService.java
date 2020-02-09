package lt.govilnius.domainService.schedule;

import io.atlassian.fugue.Either;
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
import java.util.Set;

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
            final List<Volunteer> volunteers = volunteerFilter.filterByMeet(meet);
            if (volunteers.size() > 0) {
                meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
                meetService.edit(meet.getId(), meet);
                volunteers.forEach(volunteer -> {
                    Either<Exception, MeetEngagement> meetEngagementEither = meetEngagementService.create(meet, volunteer, meet.getTime());
                    if (meetEngagementEither.isRight()) {
                        LOGGER.info("Created a meet engagement of the meet with id " + meet.getId());
                        final String token = meetEngagementEither.right().get().getToken();
                        emailSender.send(new Mail(volunteer.getEmail()), EmailSenderConfig.VOLUNTEER_REQUEST_CONFIG.apply(
                                meet, token, websiteUrl));
                    } else {
                        LOGGER.info("Fail to create a meet engagement of the meet with id " + meet.getId(), meetEngagementEither);
                    }
                });
            } else {
                meet.setStatus(Status.CANCELLATION);
                meetService.edit(meet.getId(), meet);
                emailSender.send(new Mail(meet.getEmail()), EmailSenderConfig.CANCELLATION_CONFIG.apply(meet, websiteUrl));
            }
        });
    }

    public void processRequests() {
        meetService.findByStatus(Status.SENT_VOLUNTEER_REQUEST).forEach(meet -> {
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= sentRequestWaiting) {
                LOGGER.info("Process the sent request to meet whose id " + meet.getId());
                List<MeetEngagement> meetEngagements = meetEngagementService.getByMeetId(meet.getId());
                List<Report> reports = reportService.getByMeetId(meet.getId());
                EmailSenderConfig emailSenderConfig = null;
                if (meetEngagements.size() > 0) {
                    meetEngagements = meetEngagementFilter.filterForMail(meetEngagements, meet);
                    emailSenderConfig = prepareResponsesConfig(meet, meetEngagements);
                } else if (reports.size() > 0) {
                    emailSenderConfig = prepareReportConfig(meet);
                } else {
                    LOGGER.info("Send additional information of the meet with id %s to tourist ", meet.getId());
                    emailSenderConfig = EmailSenderConfig.TOURIST_ADDITION_CONFIG.apply(meet, websiteUrl);
                    meet.setStatus(Status.SENT_TOURIST_ADDITION);
                }
                meetService.edit(meet.getId(), meet);
                emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);
            }
        });
    }

    public void processAdditionals() {
        meetService.findByStatus(Status.SENT_TOURIST_ADDITION).forEach(meet -> {
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= additionalWaiting) {
                LOGGER.info("Process the additional information of the meet with id" + meet.getId());
                meet.setStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION);
                meetService.edit(meet.getId(), meet);
                final Set<MeetEngagement> meetEngagements = meet.getMeetEngagements();
                meetEngagements.forEach(meetEngagement -> {
                    final Volunteer volunteer = meetEngagement.getVolunteer();
                    LOGGER.info("Send request to volunteer with id ", volunteer.getId());
                    emailSender.send(new Mail(volunteer.getEmail()),
                            EmailSenderConfig.VOLUNTEER_REQUEST_CONFIG.apply(meet, meetEngagement.getToken(), websiteUrl));
                });
            }
        });
    }

    public void processRequestsAfterAdditional() {
        meetService.findByStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION).forEach(meet -> {
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= sentRequestWaiting) {
                LOGGER.info("Process the sent request of the meet with id " + meet.getId());
                List<MeetEngagement> meetEngagements = meetEngagementService.getByMeetId(meet.getId());
                List<Report> reports = reportService.getByMeetId(meet.getId());
                EmailSenderConfig emailSenderConfig = null;
                if (meetEngagements.size() > 0) {
                    emailSenderConfig = prepareResponsesConfig(meet, meetEngagements);
                } else if (reports.size() > 0) {
                    emailSenderConfig = prepareReportConfig(meet);
                } else {
                    LOGGER.info("Send cancellation for the meet with id %s to tourist ", meet.getId());
                    emailSenderConfig = EmailSenderConfig.CANCELLATION_CONFIG.apply(meet, websiteUrl);
                    meet.setStatus(Status.CANCELLATION);
                }
                meetService.edit(meet.getId(), meet);
                emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);
            }
        });
    }

    public void processResponses() {
        meetService.findByStatus(Status.SENT_TOURIST_REQUEST).forEach(meet -> {
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= responsesWaiting) {
                LOGGER.info("Process responses of the meet with id " + meet.getId());
                Volunteer volunteer = meet.getVolunteer();
                if (volunteer != null) {
                    LOGGER.info("Send information of the meet " + meet.getId());
                    meet.setStatus(Status.AGREED);

                    EmailSenderConfig emailSenderConfig = EmailSenderConfig.TOURIST_INFORMATION_CONFIG.apply(meet, volunteer);
                    emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);

                    emailSenderConfig = EmailSenderConfig.VOLUNTEER_INFORMATION_CONFIG.apply(meet, volunteer);
                    emailSender.send(new Mail(volunteer.getEmail()), emailSenderConfig);
                } else {
                    LOGGER.info("Send cancellation for the meet with id %s to tourist ", meet.getId());
                    EmailSenderConfig emailSenderConfig = EmailSenderConfig.CANCELLATION_CONFIG.apply(meet, websiteUrl);
                    emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);

                    LOGGER.info("Send cancellation for the meet with id %s to volunteers ", meet.getId());
                    meetEngagementService.getByMeetId(meet.getId()).forEach(meetEngagement -> {
                        LOGGER.info("Send cancellation for the meet with id %s to volunteer with id %s ", meet.getId(), meetEngagement.getVolunteer().getId());
                        emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);
                    });
                    meet.setStatus(Status.CANCELLATION);
                }
                meetService.edit(meet.getId(), meet);
            }
        });
    }

    public void processAgreements() {
        meetService.findByStatus(Status.AGREED).forEach(meet -> {
            if (System.currentTimeMillis() - meet.getChangedAt().getTime() >= evaluationWaiting) {
                LOGGER.info("Process agreement of the meet " + meet.getId());
                Volunteer volunteer = meet.getVolunteer();
                if (volunteer != null) {
                    LOGGER.info("Send evaluations of the meet " + meet.getId());
                    EmailSenderConfig emailSenderConfig = EmailSenderConfig.TOURIST_EVALUATION_CONFIG.apply(meet, websiteUrl);
                    emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);

                    emailSenderConfig = EmailSenderConfig.VOLUNTEER_EVALUATION_CONFIG.apply(meet, websiteUrl);
                    emailSender.send(new Mail(volunteer.getEmail()), emailSenderConfig);

                    meet.setStatus(Status.FINISHED);
                    meetService.edit(meet.getId(), meet);
                } else {
                    LOGGER.error("Fail to send evaluations of the meet " + meet.getId());
                    meet.setStatus(Status.ERROR);
                    meetService.edit(meet.getId(), meet);
                }
            }
        });
    }

    private EmailSenderConfig prepareReportConfig(Meet meet) {
        LOGGER.info("Send report of the meet with id %s to tourist ", meet.getId());
        meet.setStatus(Status.REPORTED);
        return EmailSenderConfig.REPORT_CONFIG.apply(meet, websiteUrl);
    }

    private EmailSenderConfig prepareResponsesConfig(Meet meet, List<MeetEngagement> meetEngagements) {
        LOGGER.info("Send volunteers responses of the meet with id %s to tourist ", meet.getId());
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        return EmailSenderConfig.TOURIST_REQUEST_CONFIG.apply(meet, meetEngagements, websiteUrl);
    }
}
