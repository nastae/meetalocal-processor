package lt.govilnius.domainService.mail;

import org.springframework.stereotype.Service;

@Service
public class MeetingMailService {

    /*
    @Autowired
    private EmailSender emailSender;

    @Autowired
    private VolunteerFilter volunteerFilter;

    @Autowired
    private AgreementService agreementService;

    @Value("${website.url}")
    private String websiteUrl;
*/
    /*
    public void sendMeetRequests(Meet form) {
        volunteerFilter.filterByForm(form).forEach(volunteer -> {
//                final Either<Exception, MeetingAgreement> agreement = agreementService.create(new MeetingAgreement(form, volunteer, false));
                Map<String, java.io.Serializable> model = new HashMap<String, java.io.Serializable>();
                model.put("month", DateUtils.monthToLithuanian(form.getMeetingDate().getMonth()));
                model.put("day", form.getMeetingDate().getDate());
                model.put("time",  String.format("%02d:%02d", form.getMeetingDate().getHours(), form.getMeetingDate().getMinutes()));
                model.put("name", form.getNameAndLastname());
                model.put("gender", form.getGender());
                model.put("age", form.getAge());
                model.put("peopleCount", form.getPeopleCount());
//                model.put("id", agreement.right().get().getMeetingForm().getId());
                model.put("preferences", form.getPreferences());
                model.put("canLink", websiteUrl + "");
                model.put("cannotLink", websiteUrl + "");
                emailSender.send(new Mail(volunteer.getEmail(),
                        "Meet a Local susitikimas su " + form.getNameAndLastname() + " (" + form.getId() + ")"),
                        model, "mails/request-to-meet.vm");
        });
    }

    public void sendAgreements(List<Volunteer> volunteers, Meet form) {
            final Map<String, Object> model = new HashMap<String, Object>();
            model.put("name", form.getNameAndLastname());
            model.put("volunteers", volunteers);
            emailSender.send(new Mail(form.getEmail(), "Meet a Local in Vilnius"),
                    model, "mails/agreements.vm");
    }
    */

    /*
    public void sendVolunteerNotFound(Meet form) {
        final Map<String, String> model = new HashMap<String, String>();
        model.put("name", form.getNameAndLastname());
        emailSender.send(new Mail(form.getEmail(), "Meet a Local in Vilnius"), model,
                "mails/volunteer-not-found.vm");
    }
    */
/*
    public void sendInformationToTourist(Meet form, Volunteer volunteer) {
        final Map<String, java.io.Serializable> model = new HashMap<>();
        model.put("name", form.getNameAndLastname());
        model.put("day", form.getMeetingDate().getDate());
        model.put("month", DateUtils.monthToEnglish(form.getMeetingDate().getMonth()));
        model.put("time", String.format("%02d:%02d", form.getMeetingDate().getHours(), form.getMeetingDate().getMinutes()));
        model.put("meetFriend", volunteer.getName());
        model.put("phoneNumber", volunteer.getPhoneNumber());
        model.put("email", volunteer.getEmail());
        emailSender.send(new Mail(form.getEmail(), "Meet a Local in Vilnius"),
                model, "mails/information-for-tourist.vm");
    }
*/
/*
    public void sendInformationToVolunteer(Meet form, Volunteer volunteer) {
        final Map<String, java.io.Serializable> model = new HashMap<String, java.io.Serializable>();
        model.put("day", form.getMeetingDate().getDate());
        model.put("month", DateUtils.monthToLithuanian(form.getMeetingDate().getMonth()));
        model.put("time", String.format("%02d:%02d", form.getMeetingDate().getHours(), form.getMeetingDate().getMinutes()));
        model.put("meetFriend", form.getNameAndLastname());
        model.put("phoneNumber", form.getPhoneNumber());
        model.put("email", form.getEmail());
        emailSender.send(new Mail(volunteer.getEmail(), "Meet a Local in Vilnius"),
                model, "mails/information-for-volunteer.vm");
    }
*/

/*
    public void sendNotSelectedVolunteer(Volunteer volunteer) {
        emailSender.send(new Mail(volunteer.getEmail(), "Meet a Local in Vilnius"),
                new HashMap(), "mails/nobody-selected-volunteer.vm");
    }

    public void sendEvaluation(Meet form) {
        emailSender.send(new Mail(form.getEmail(), "Meet a Local in Vilnius"),
                new HashMap(), "mails/evaluation.vm");
    }
    */
}
