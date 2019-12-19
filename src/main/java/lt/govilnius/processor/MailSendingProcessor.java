package lt.govilnius.processor;

import lt.govilnius.mail.MeetingMailService;
import lt.govilnius.models.MeetingForm;
import lt.govilnius.models.Status;
import lt.govilnius.models.Volunteer;
import lt.govilnius.repository.entity.MeetingAgreementEntity;
import lt.govilnius.services.MeetingAgreementService;
import lt.govilnius.services.MeetingFormService;
import lt.govilnius.services.VolunteerService;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
public class MailSendingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSendingProcessor.class);

    @Autowired
    private MeetingMailService meetingMailService;

    @Autowired
    private MeetingFormService meetingFormService;

    @Autowired
    private MeetingAgreementService agreementService;

    @Autowired
    private VolunteerService volunteerService;

    @Scheduled(fixedDelay = 5000L)
    public void sendRequests() {
        meetingFormService.findByStatus(Status.NEW).forEach(formEntity -> {
            LOGGER.info("Process the new meeting form with id " + formEntity.getId());
            formEntity.setStatus(Status.SENT);
            final MeetingForm form =  meetingFormService.fromEntity(formEntity);
            meetingFormService.edit(formEntity.getId(), form);
            meetingMailService.sendMeetRequests(form);
        });
    }

    @Scheduled(fixedDelay = 5000L)
    public void sendAgreements() {
        meetingFormService.findByStatus(Status.SENT).forEach(formEntity -> {
            LOGGER.info("Process the sent meeting form with id " + formEntity.getId());
            formEntity.setStatus(Status.AGREED);
            final MeetingForm form = meetingFormService.fromEntity(formEntity);
            meetingFormService.edit(formEntity.getId(), form);
            final List<Volunteer> volunteers = volunteerService.findAgreementVolunteers(agreementService.findAgreedAgreements(formEntity));
            if (volunteers.size() == 0) {
                LOGGER.info("Not found volunteers with the meeting form id " + formEntity.getId());
                meetingMailService.sendVolunteerNotFound(form);
            } else {
                LOGGER.info("Send agreements to the meeting form with id " + formEntity.getId());
                meetingMailService.sendAgreements(volunteers, form);
            }
        });
    }

    @Scheduled(fixedDelay = 5000L)
    public void sendInformation() {
        meetingFormService.findByStatus(Status.AGREED).forEach(formEntity -> {
            if (formEntity.getVolunteerEntity() != null) {
                LOGGER.info("Process the agreed meeting form with id " + formEntity.getId());
                final Volunteer volunteer = volunteerService.fromEntity(formEntity.getVolunteerEntity());
                LOGGER.info("Processing form has volunteer with id " + formEntity.getVolunteerEntity().getId());
                formEntity.setStatus(Status.FINISHED);
                final MeetingForm form = meetingFormService.fromEntity(formEntity);
                meetingFormService.edit(formEntity.getId(), form);

                LOGGER.info("Send the meeting information to tourist with form id " + form.getId());
                meetingMailService.sendInformationToTourist(form, volunteer);
                LOGGER.info("Send the meeting information to volunteer with volunteer id " + volunteer.getId());
                meetingMailService.sendInformationToVolunteer(form, volunteer);

                agreementService.findByIsAgreedAndMeetingForm(true, formEntity)
                        .stream()
                        .filter(a -> !a.getVolunteerEntity().getId().equals(volunteer.getId()))
                        .map(MeetingAgreementEntity::getVolunteerEntity)
                        .forEach(v -> {
                            LOGGER.info("Send not selecting message to volunteer with id " + volunteer.getId());
                            meetingMailService.sendNotSelectedVolunteer(volunteer);
                        });
            }
        });
    }

    @Scheduled(fixedDelay = 5000L)
    public void sendEvaluation() {
        meetingFormService.findByStatus(Status.FINISHED).forEach(formEntity -> {
            if (Hours.hoursBetween(
                    new DateTime(formEntity.getMeetingDate()),
                    new DateTime(Calendar.getInstance().getTime())).isGreaterThan(Hours.hours(24))) {
                LOGGER.info("Process the finished meeting form with id " + formEntity.getId());
                formEntity.setStatus(Status.EVALUATED);
                final MeetingForm form = meetingFormService.fromEntity(formEntity);
                meetingFormService.edit(formEntity.getId(), form);
                LOGGER.info("Send the evaluation for the form with id " + formEntity.getId());
                meetingMailService.sendEvaluation(form);
            }
        });
    }
}
