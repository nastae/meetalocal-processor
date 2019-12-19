package lt.govilnius.mail;

import io.atlassian.fugue.Either;
import lt.govilnius.models.MeetingAgreement;
import lt.govilnius.models.MeetingForm;
import lt.govilnius.models.Volunteer;
import lt.govilnius.processor.VolunteerFilter;
import lt.govilnius.services.Error;
import lt.govilnius.services.MeetingAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MeetingMailService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private VolunteerFilter volunteerFilter;

    @Autowired
    private MeetingAgreementService meetingAgreementService;

    public void sendMeetRequests(MeetingForm form) {
        volunteerFilter.filterByForm(form).forEach(volunteer -> {
                Either<Error, MeetingAgreement> agreementEither = meetingAgreementService.create(new MeetingAgreement(form.getId().longValue(), volunteer.getId(), false));
                Map<String, java.io.Serializable> model = new HashMap<String, java.io.Serializable>();
                model.put("month", DateUtils.monthToLithuanian(form.getMeetingDate().getMonth()));
                model.put("day", form.getMeetingDate().getDay());
                model.put("time",  String.format("%02d:%02d", form.getMeetingDate().getHours(), form.getMeetingDate().getMinutes()));
                model.put("name", form.getNameAndLastname());
                model.put("gender", form.getGender());
                model.put("age", form.getAge());
                model.put("peopleCount", form.getPeopleCount());
                model.put("id", agreementEither.right().get().getId());
                //model.put("hobbies", );
                model.put("preferences", form.getPreferences());
                emailService.send(new Mail(volunteer.getEmail(),
                        "Meet a Local susitikimas su " + form.getNameAndLastname() + " (" + form.getId() + ")"),
                        model, "./templates/request-to-meet.vm");
        });
    }

    public void sendAgreements(List<Volunteer> volunteers, MeetingForm form) {
            final Map<String, Object> model = new HashMap<String, Object>();
            model.put("name", form.getNameAndLastname());
            model.put("volunteers", volunteers);
            emailService.send(new Mail(form.getEmail(), "Meet a Local in Vilnius"),
                    model, "./templates/agreements.vm");
    }

    public void sendVolunteerNotFound(MeetingForm form) {
        final Map<String, String> model = new HashMap<String, String>();
        model.put("name", form.getNameAndLastname());
        emailService.send(new Mail(form.getEmail(), "Meet a Local in Vilnius"), model,
                "./templates/volunteer-not-found.vm");
    }

    public void sendInformationToTourist(MeetingForm form, Volunteer volunteer) {
        final Map<String, java.io.Serializable> model = new HashMap<>();
        model.put("name", form.getNameAndLastname());
        model.put("day", form.getMeetingDate().getDay());
        model.put("month", DateUtils.monthToEnglish(form.getMeetingDate().getMonth()));
        model.put("time", String.format("%02d:%02d", form.getMeetingDate().getHours(), form.getMeetingDate().getMinutes()));
        model.put("meetFriend", volunteer.getName());
        model.put("phoneNumber", volunteer.getPhoneNumber());
        model.put("email", volunteer.getEmail());
        emailService.send(new Mail(form.getEmail(), "Meet a Local in Vilnius"),
                model, "./templates/information-for-tourist.vm");
    }

    public void sendInformationToVolunteer(MeetingForm form, Volunteer volunteer) {
        final Map<String, java.io.Serializable> model = new HashMap<String, java.io.Serializable>();
        model.put("day", form.getMeetingDate().getDay());
        model.put("month", DateUtils.monthToLithuanian(form.getMeetingDate().getMonth()));
        model.put("time", String.format("%02d:%02d", form.getMeetingDate().getHours(), form.getMeetingDate().getMinutes()));
        model.put("meetFriend", form.getNameAndLastname());
        model.put("phoneNumber", form.getPhoneNumber());
        model.put("email", form.getEmail());
        emailService.send(new Mail(volunteer.getEmail(), "Meet a Local in Vilnius"),
                model, "./templates/information-for-volunteer.vm");
    }

    public void sendNotSelectedVolunteer(Volunteer volunteer) {
        emailService.send(new Mail(volunteer.getEmail(), "Meet a Local in Vilnius"),
                new HashMap(), "./templates/nobody-selected-volunteer.vm");
    }

    public void sendEvaluation(MeetingForm form) {
        emailService.send(new Mail(form.getEmail(), "Meet a Local in Vilnius"),
                new HashMap(), "./templates/evaluation.vm");
    }
}
