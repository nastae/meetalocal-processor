package lt.govilnius.domainService.schedule;

import lt.govilnius.domain.reservation.Status;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.domainService.mail.EmailSender;
import lt.govilnius.domainService.mail.EmailSenderConfig;
import lt.govilnius.domainService.mail.Mail;
import lt.govilnius.facadeService.reservation.MeetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MailSendingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSendingProcessor.class);

    @Autowired
    private MeetService meetService;

    @Autowired
    private EmailSender emailSender;

    @Value("${website.url}")
    private String websiteUrl;

    @Scheduled(fixedDelay = 5000L)
    public void processNew() {
        meetService.findByStatus(Status.NEW).forEach(meet -> {
            LOGGER.info("Process the new meeting form with id " + meet.getId());
            meet.setStatus(Status.SENT_REQUEST);
            meetService.edit(meet.getId(), meet);
            emailSender.send(new Mail(meet.getEmail()), EmailSenderConfig.AGREEMENT_REQUEST_CONFIG.apply(meet, websiteUrl));
        });
    }

    @Scheduled(fixedDelay = 5000L)
    public void processRequests() {
        meetService.findByStatus(Status.SENT_REQUEST).forEach(meet -> {
            LOGGER.info("Process the sent request to meet whose id " + meet.getId());
//            final List<Volunteer> volunteers = new ArrayList<>(meet.getEngagedVolunteers());
            EmailSenderConfig emailSenderConfig = null;
//            if (volunteers.size() > 0) {
                // ar yra reportas

                LOGGER.info("Send agreements to the meeting form with id " + meet.getId());
                meet.setStatus(Status.SENT_RESPONSE);
                //emailSenderConfig = EmailSenderConfig.AGREEMENT_RESPONSE_CONFIG.apply(meet, volunteers, websiteUrl);

         /*   } else {
                LOGGER.info("Not found volunteers with the meeting form id " + meet.getId());
                meet.setStatus(Status.VOLUNTEER_NOT_FOUND);
                emailSenderConfig = EmailSenderConfig.AGREEMENT_RESPONSE_NOT_FOUND_CONFIG.apply(meet, websiteUrl);
            }*/
            meetService.edit(meet.getId(), meet);
            emailSender.send(new Mail(meet.getEmail()), emailSenderConfig);
        });
    }

    @Scheduled(fixedDelay = 5000L)
    public void processVolunteerNotFoundAgreements() {
        meetService.findByStatus(Status.VOLUNTEER_NOT_FOUND).forEach(meet -> {
            // additional msg
        });
    }

    @Scheduled(fixedDelay = 5000L)
    public void processResponses() {
        meetService.findByStatus(Status.SENT_RESPONSE).forEach(meet -> {
            Volunteer volunteer = meet.getVolunteer();
            if (volunteer != null) {
                LOGGER.info("Process the sent response meeting form with id " + meet.getId());
                LOGGER.info("Processing form has volunteer with id " + meet.getVolunteer().getId());
                meet.setStatus(Status.AGREED);
                meetService.edit(meet.getId(), meet);
            }
        });
    }

    @Scheduled(fixedDelay = 5000L)
    public void processAgrees() {
        meetService.findByStatus(Status.AGREED).forEach(meet -> {
            Volunteer volunteer = meet.getVolunteer();
            LOGGER.info("Process the agreed meeting form with id " + meet.getId());
            LOGGER.info("Processing form has volunteer with id " + meet.getVolunteer().getId());
            if (volunteer != null) {
                meet.setStatus(Status.FINISHED);
                meetService.edit(meet.getId(), meet);

                LOGGER.info("Send the meeting information to tourist with form id " + meet.getId());
                emailSender.send(
                        new Mail(meet.getEmail()), EmailSenderConfig.TOURIST_INFORMATION_CONFIG.apply(meet, volunteer));
                LOGGER.info("Send the meeting information to volunteer with volunteer id " + volunteer.getId());
                emailSender.send(
                        new Mail(meet.getEmail()), EmailSenderConfig.VOLUNTEER_INFORMATION_CONFIG.apply(meet, volunteer));

                /*
                agreementService.findByIsAgreedAndMeetingForm(true, meet)
                        .stream()
                        .filter(a -> !a.getVolunteer().getId().equals(volunteer.getId()))
                        .map(MeetingAgreement::getVolunteer)
                        .forEach(v -> {
                            LOGGER.info("Send not selecting message to volunteer with id " + volunteer.getId());
//                            meetingMailService.sendNotSelectedVolunteer(volunteer);
                        });
                        */
            } else {
                LOGGER.info("The form set not selected which id " + meet.getVolunteer().getId());
                meet.setStatus(Status.VOLUNTEER_NOT_SELECTED);
                meetService.edit(meet.getId(), meet);
            }
        });
    }

    @Scheduled(fixedDelay = 5000L)
    public void processVolunteersNotSelectedAgreements() {
        meetService.findByStatus(Status.VOLUNTEER_NOT_SELECTED).forEach(meet -> {
            emailSender.send(
                    new Mail(meet.getEmail()), EmailSenderConfig.AGREEMENT_RESPONSE_NOT_SELECTED_CONFIG.apply(meet, websiteUrl));
        });
    }

    @Scheduled(fixedDelay = 5000L)
    public void processFinsihedAgreements() {
        meetService.findByStatus(Status.FINISHED).forEach(meet -> {
            // jei date > change date
            // siuncia
        });
    }

    @Scheduled(fixedDelay = 5000L)
    public void processEvaluations() {
        meetService.findByStatus(Status.EVALUATED).forEach(meet -> {
            //
        });
    }

    /*
    @Scheduled(fixedDelay = 5000L)
    public void sendInformation() {
        meetService.findByStatus(Status.AGREED).forEach(form -> {
            imeet (form.getVolunteer() != null) {
                LOGGER.info("Process the agreed meeting form with id " + form.getId());
                final Volunteer volunteer = form.getVolunteer();
                LOGGER.info("Processing form has volunteer with id " + form.getVolunteer().getId());
                form.setStatus(Status.FINISHED);
                meetService.edit(form.getId(), form);

                LOGGER.info("Send the meeting information to tourist with form id " + form.getId());
                meetingMailService.sendInformationToTourist(form, volunteer);
                LOGGER.info("Send the meeting information to volunteer with volunteer id " + volunteer.getId());
                meetingMailService.sendInformationToVolunteer(form, volunteer);

                agreementService.findByIsAgreedAndMeetingForm(true, form)
                        .stream()
                        .filter(a -> !a.getVolunteer().getId().equals(volunteer.getId()))
                        .map(MeetingAgreement::getVolunteer)
                        .forEach(v -> {
                            LOGGER.info("Send not selecting message to volunteer with id " + volunteer.getId());
                            meetingMailService.sendNotSelectedVolunteer(volunteer);
                        });
            }
        });
    }

    @Scheduled(fixedDelay = 5000L)
    public void sendEvaluation() {
        meetService.findByStatus(Status.FINISHED).forEach(form -> {
            imeet (Hours.hoursBetween(
                    new DateTime(form.getMeetingDate()),
                    new DateTime(Calendar.getInstance().getTime())).isGreaterThan(Hours.hours(24))) {
                LOGGER.info("Process the finished meeting form with id " + form.getId());
                form.setStatus(Status.EVALUATED);
                meetService.edit(form.getId(), form);
                LOGGER.info("Send the evaluation for the form with id " + form.getId());
                meetingMailService.sendEvaluation(form);
            }
        });
    }
    */
}
