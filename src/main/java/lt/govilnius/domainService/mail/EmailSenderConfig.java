package lt.govilnius.domainService.mail;

import com.google.common.collect.ImmutableMap;
import com.speedment.common.function.TriFunction;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.Volunteer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class EmailSenderConfig {

    private static final String MAIL_TEMPLATE_PATH = "mails";

    private String subject;
    private Template template;
    private Map<String, Object> model;

    public EmailSenderConfig(Template template, Map<String, Object> model, String subject) {
        this.template = template;
        this.model = model;
        this.subject = subject;
    }

    public static final BiFunction<Meet, String, EmailSenderConfig> AGREEMENT_REQUEST_CONFIG = (meet, websiteUrl) ->
        new EmailSenderConfig(Template.AGREEMENT_REQUEST, ImmutableMap
                .<String, Object>builder()
                .put("month", DateUtils.monthToLithuanian(meet.getDate().getMonth()))
                .put("day", meet.getDate().getDate())
                .put("time", String.format("%02d:%02d", meet.getDate().getHours(), meet.getDate().getMinutes()))
                .put("name", meet.getName())
                .put("gender", meet.getGender().getName())
                .put("age", meet.getAge())
                .put("peopleCount", meet.getPeopleCount())
                .put("preferences", meet.getPreferences())
                .put("canLink", websiteUrl + "")
                .put("cannotLink", websiteUrl + "")
                .build(),
                "Meet a Local susitikimas su " + meet.getName());

    public static final TriFunction<Meet, List<Volunteer>, String, EmailSenderConfig> AGREEMENT_RESPONSE_CONFIG = (meet, volunteers, websiteUrl) -> {
        final Map<String, Object> model = new HashMap<>();
        model.put("name", meet.getName());
        model.put("volunteers", volunteers);
        return new EmailSenderConfig(Template.AGREEMENT_RESPONSE, model, "Meet a Local in Vilnius");
    };

    public static final BiFunction<Meet, String, EmailSenderConfig> AGREEMENT_RESPONSE_NOT_FOUND_CONFIG = (f, websiteUrl) ->
        new EmailSenderConfig(Template.AGREEMENT_RESPONSE_NOT_FOUND, ImmutableMap
                .<String, Object>builder()
                .put("name", f.getName())
                .build(),
                "Meet a Local in Vilnius");

    public static final BiFunction<Meet, Volunteer, EmailSenderConfig> TOURIST_INFORMATION_CONFIG = (f, v) ->
        new EmailSenderConfig(Template.TOURIST_INFORMATION, ImmutableMap
                .<String, Object>builder()
                .put("name", f.getName())
                .put("month", DateUtils.monthToEnglish(f.getDate().getMonth()))
                .put("time", String.format("%02d:%02d", f.getDate().getHours(), f.getDate().getMinutes()))
                .put("meetFriend", v.getName())
                .put("phoneNumber", v.getPhoneNumber())
                .put("email", v.getEmail())
                .build(),
                "Meet a Local in Vilnius");

    public static final BiFunction<Meet, Volunteer, EmailSenderConfig> VOLUNTEER_INFORMATION_CONFIG = (f, v) ->
        new EmailSenderConfig(Template.VOLUNTEER_INFORMATION, ImmutableMap
                .<String, Object>builder()
                .put("month", DateUtils.monthToLithuanian(f.getDate().getMonth()))
                .put("time", String.format("%02d:%02d", f.getDate().getHours(), f.getDate().getMinutes()))
                .put("meetFriend", f.getName())
                .put("phoneNumber", f.getPhoneNumber())
                .put("email", f.getEmail())
                .build(),
                "Meet a Local in Vilnius");

    public static final BiFunction<Meet, String, EmailSenderConfig> EVALUATION_CONFIG = (f, websiteUrl) ->
        new EmailSenderConfig(Template.EVALUATION, ImmutableMap.of(), "Meet a Local in Vilnius");

    public static final BiFunction<Meet, String, EmailSenderConfig> AGREEMENT_RESPONSE_NOT_SELECTED_CONFIG = (m, websiteUrl) ->
        new EmailSenderConfig(Template.AGREEMENT_RESPONSE_NOT_SELECTED, ImmutableMap.of(), "Meet a Local in Vilnius");

    public static final BiFunction<Meet, String, EmailSenderConfig> AGREEMENT_REPONSE_REPORTED = (f, websiteUrl) ->
        new EmailSenderConfig(Template.AGREEMENT_RESPONSE_REPORTED, ImmutableMap.of(), "Meet a local in Vilnius");

    public Template getTemplate() {
        return template;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public String getSubject() {
        return subject;
    }

    /**
     * @Template - E-mail template
     */
    public enum Template {
        AGREEMENT_REQUEST(MAIL_TEMPLATE_PATH + "/agreement-request.vm"),
        AGREEMENT_RESPONSE(MAIL_TEMPLATE_PATH + "/agreement-response.vm"),
        AGREEMENT_RESPONSE_NOT_FOUND(MAIL_TEMPLATE_PATH + "/agreement-response-not-found.vm"),
        AGREEMENT_RESPONSE_NOT_SELECTED(MAIL_TEMPLATE_PATH + "/agreement-response-not-selected.vm"),
        AGREEMENT_RESPONSE_REPORTED(MAIL_TEMPLATE_PATH + "/agreement-response-reported.vm"),
        EVALUATION(MAIL_TEMPLATE_PATH + "/evaluation.vm"),
        TOURIST_INFORMATION(MAIL_TEMPLATE_PATH + "/tourist-information.vm"),
        VOLUNTEER_INFORMATION(MAIL_TEMPLATE_PATH + "/volunteer-information.vm");

        public final String path;

        private Template(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}