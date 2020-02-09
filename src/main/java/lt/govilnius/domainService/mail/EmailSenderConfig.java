package lt.govilnius.domainService.mail;

import com.google.common.collect.ImmutableMap;
import com.speedment.common.function.TriFunction;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Volunteer;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static java.lang.String.format;

public class EmailSenderConfig {

    private static final String MAIL_TEMPLATE_PATH = "mails";

    private String subject;
    private Template template;
    private Map<String, Object> model;

    private static final String SUBJECT = "Meet a local";

    public EmailSenderConfig(Template template, Map<String, Object> model, String subject) {
        this.template = template;
        this.model = model;
        this.subject = subject;
    }

    public static final TriFunction<Meet, String, String, EmailSenderConfig> VOLUNTEER_REQUEST_CONFIG = (meet, token, websiteUrl) -> {
        Calendar cal = Calendar.getInstance();
        cal.setTime(meet.getDate());
        return new EmailSenderConfig(Template.VOLUNTEER_REQUEST, ImmutableMap
                .<String, Object>builder()
                .put("month", DateUtils.monthToLithuanian(cal.get(Calendar.MONTH)))
                .put("day", cal.get(Calendar.DAY_OF_MONTH))
                .put("time", format("%02d:%02d", 1, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE)))
                .put("name", meet.getName())
                .put("gender", meet.getGender().getName())
                .put("age", meet.getAge())
                .put("peopleCount", meet.getPeopleCount())
                .put("preferences", meet.getPreferences())
                .put("agreementUrl", websiteUrl + "/mail/agreements?token=" + token)
                .put("cancellationUrl", websiteUrl + "/mail/cancellations?token=" + token)
                .put("changeUrl", websiteUrl + "/mail/engagements-changes?token=" + token)
                .put("reportUrl", websiteUrl + "/mail/reports?token=" + token)
                .put("token", token)
                .build(),
                format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final TriFunction<Meet, List<MeetEngagement>, String, EmailSenderConfig> TOURIST_REQUEST_CONFIG = (meet, engagements, websiteUrl) -> {
        final Map<String, Object> model = new HashMap<>();
        model.put("name", meet.getName());
        model.put("engagements", engagements);
        model.put("websiteUrl", websiteUrl);

        return new EmailSenderConfig(Template.TOURIST_REQUEST, model, format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final BiFunction<Meet, String, EmailSenderConfig> TOURIST_ADDITION_CONFIG = (meet, websiteUrl) ->
        new EmailSenderConfig(Template.VOLUNTEER_ADDITION, ImmutableMap
                .<String, Object>builder()
                .put("name", meet.getName())
                .put("changeMeetAfterAdditionUrl", websiteUrl + "/mail/meets-changes?meet=" + meet.getId().toString())
                .build(),
                format("%s # %07d", SUBJECT, meet.getId()));

    public static final BiFunction<Meet, Volunteer, EmailSenderConfig> TOURIST_INFORMATION_CONFIG = (meet, v) -> {
        Calendar cal = Calendar.getInstance();
        cal.setTime(meet.getDate());
        return new EmailSenderConfig(Template.TOURIST_INFORMATION, ImmutableMap
                .<String, Object>builder()
                .put("name", meet.getName())
                .put("month", DateUtils.monthToEnglish(cal.get(Calendar.MONTH)))
                .put("time", format("%02d:%02d", 1, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE)))
                .put("meetFriend", v.getName())
                .put("phoneNumber", v.getPhoneNumber())
                .put("email", v.getEmail())
                .build(),
                format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final BiFunction<Meet, Volunteer, EmailSenderConfig> VOLUNTEER_INFORMATION_CONFIG = (meet, v) -> {
        Calendar cal = Calendar.getInstance();
        cal.setTime(meet.getDate());
        return new EmailSenderConfig(Template.VOLUNTEER_INFORMATION, ImmutableMap
                .<String, Object>builder()
                .put("month", DateUtils.monthToLithuanian(cal.get(Calendar.MONTH)))
                .put("time", format("%02d:%02d", 1, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE)))
                .put("meetFriend", meet.getName())
                .put("phoneNumber", meet.getPhoneNumber())
                .put("email", meet.getEmail())
                .build(),
                format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final BiFunction<Meet, String, EmailSenderConfig> TOURIST_EVALUATION_CONFIG = (meet, websiteUrl) ->
        new EmailSenderConfig(Template.TOURIST_EVALUATION, ImmutableMap.of(),
                format("%s # %07d", SUBJECT, meet.getId()));

    public static final BiFunction<Meet, String, EmailSenderConfig> VOLUNTEER_EVALUATION_CONFIG = (meet, websiteUrl) ->
            new EmailSenderConfig(Template.VOLUNTEER_EVALUATION, ImmutableMap.of(),
                    format("%s # %07d", SUBJECT, meet.getId()));

    public static final BiFunction<Meet, String, EmailSenderConfig> CANCELLATION_CONFIG = (meet, websiteUrl) ->
        new EmailSenderConfig(Template.CANCELLATION, ImmutableMap.of(),
                format("%s # %07d", SUBJECT, meet.getId()));

    public static final BiFunction<Meet, String, EmailSenderConfig> REPORT_CONFIG = (meet, websiteUrl) ->
        new EmailSenderConfig(Template.REPORT, ImmutableMap.of(),
                format("%s # %07d", SUBJECT, meet.getId()));

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
        VOLUNTEER_REQUEST(MAIL_TEMPLATE_PATH + "/volunteer-request.vm"),
        TOURIST_REQUEST(MAIL_TEMPLATE_PATH + "/tourist-request.vm"),
        VOLUNTEER_ADDITION(MAIL_TEMPLATE_PATH + "/tourist-addition.vm"),
        CANCELLATION(MAIL_TEMPLATE_PATH + "/cancellation.vm"),
        REPORT(MAIL_TEMPLATE_PATH + "/tourist-report.vm"),
        TOURIST_EVALUATION(MAIL_TEMPLATE_PATH + "/tourist-evaluation.vm"),
        VOLUNTEER_EVALUATION(MAIL_TEMPLATE_PATH + "/volunteer-evaluation.vm"),
        TOURIST_INFORMATION(MAIL_TEMPLATE_PATH + "/tourist-information.vm"),
        VOLUNTEER_INFORMATION(MAIL_TEMPLATE_PATH + "/volunteer-information.vm"),;

        public final String path;

        private Template(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}