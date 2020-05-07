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
import java.util.stream.Collectors;

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
        Calendar time = Calendar.getInstance();
        time.setTime(meet.getTime());
        String languages = meet.getLanguages()
                .stream()
                .map(l -> l.getLanguage().getName())
                .collect(Collectors.joining(", "));
        return new EmailSenderConfig(Template.VOLUNTEER_REQUEST, ImmutableMap
                .<String, Object>builder()
                .put("month", cal.get(Calendar.MONTH))
                .put("day", cal.get(Calendar.DAY_OF_MONTH))
                .put("hours", format("%02d", time.get(Calendar.HOUR_OF_DAY)))
                .put("minutes", format("%02d", time.get(Calendar.MINUTE)))
                .put("name", meet.getName())
                .put("languages", languages)
                .put("age", meet.getAge())
                .put("count", meet.getPeopleCount())
                .put("preferences", meet.getPreferences())
                .put("additionalPreferences", meet.getAdditionalPreferences())
                .put("agreementUrl", websiteUrl + "/volunteer-action-management/agreements?token=" + token)
                .put("cancellationUrl", websiteUrl + "/volunteer-action-management/cancellations?token=" + token)
                .put("editUrl", websiteUrl + "/volunteer-action-management/engagements?token=" + token)
                .build(),
                format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final TriFunction<Meet, List<MeetEngagement>, String, EmailSenderConfig> TOURIST_REQUEST_CONFIG = (meet, engagements, websiteUrl) -> {
        final Map<String, Object> model = new HashMap<>();
        model.put("name", meet.getName());
        model.put("engagements", engagements
                .stream()
                .filter(e -> e.getTime().equals(meet.getTime()))
                .collect(Collectors.toList()));
        model.put("engagementsWithEditedTime", engagements
                .stream()
                .filter(e -> !(e.getTime().equals(meet.getTime())))
                .collect(Collectors.toList()));
        model.put("websiteUrl", websiteUrl);

        return new EmailSenderConfig(Template.TOURIST_REQUEST, model, format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final TriFunction<Meet, Volunteer, MeetEngagement, EmailSenderConfig> TOURIST_INFORMATION_CONFIG = (meet, v, e) -> {
        Calendar date = Calendar.getInstance();
        date.setTime(meet.getDate());
        Calendar time = Calendar.getInstance();
        time.setTime(e.getTime());
        return new EmailSenderConfig(Template.TOURIST_INFORMATION, ImmutableMap
                .<String, Object>builder()
                .put("name", meet.getName())
                .put("month", DateUtils.monthToEnglish(date.get(Calendar.MONTH)))
                .put("day", String.valueOf(date.get(Calendar.DAY_OF_MONTH)))
                .put("hours", format("%02d", time.get(Calendar.HOUR_OF_DAY)))
                .put("minutes", format("%02d", time.get(Calendar.MINUTE)))
                .put("friendName", v.getName())
                .put("phoneNumber", v.getPhoneNumber())
                .put("email", v.getEmail())
                .build(),
                format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final TriFunction<Meet, Volunteer, MeetEngagement, EmailSenderConfig> VOLUNTEER_INFORMATION_CONFIG = (meet, v, e) -> {
        Calendar date = Calendar.getInstance();
        date.setTime(meet.getDate());
        Calendar time = Calendar.getInstance();
        time.setTime(e.getTime());
        return new EmailSenderConfig(Template.VOLUNTEER_INFORMATION, ImmutableMap
                .<String, Object>builder()
                .put("month", date.get(Calendar.MONTH))
                .put("day", String.valueOf(date.get(Calendar.DAY_OF_MONTH)))
                .put("hours", format("%02d", time.get(Calendar.HOUR_OF_DAY)))
                .put("minutes", format("%02d", time.get(Calendar.MINUTE)))
                .put("name", v.getName())
                .put("phoneNumber", meet.getPhoneNumber())
                .put("email", meet.getEmail())
                .build(),
                format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final BiFunction<MeetEngagement, String, EmailSenderConfig> TOURIST_EVALUATION_CONFIG = (engagement, websiteUrl) -> {
        final Meet meet = engagement.getMeet();
        return new EmailSenderConfig(Template.TOURIST_EVALUATION, ImmutableMap
                .<String, Object>builder()
                .put("name", meet.getName())
                .put("evaluationUrl", websiteUrl + "/tourist-action-management/evaluations?token=" + engagement.getToken())
                .build(),
                format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final BiFunction<MeetEngagement, String, EmailSenderConfig> VOLUNTEER_EVALUATION_CONFIG = (engagement, websiteUrl) -> {
        final Meet meet = engagement.getMeet();
        return new EmailSenderConfig(Template.VOLUNTEER_EVALUATION, ImmutableMap
                .<String, Object>builder()
                .put("evaluationUrl", websiteUrl + "/volunteer-action-management/evaluations?token=" + engagement.getToken())
                .build(),
                format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final BiFunction<Meet, String, EmailSenderConfig> VOLUNTEER_CANCELLATION_CONFIG = (meet, websiteUrl) ->
            new EmailSenderConfig(Template.VOLUNTEER_CANCELLATION, ImmutableMap.of(),
                    format("%s # %07d", SUBJECT, meet.getId()));

    public static final BiFunction<Meet, String, EmailSenderConfig> TOURIST_CANCELLATION_CONFIG = (meet, registrationUrl) ->
            new EmailSenderConfig(Template.TOURIST_CANCELLATION,
                    ImmutableMap
                            .<String, Object>builder()
                            .put("name", meet.getName())
                            .put("registrationUrl", registrationUrl)
                            .build(),
                    format("%s # %07d", SUBJECT, meet.getId()));

    public static final BiFunction<Meet, String, EmailSenderConfig> TOURIST_CANCELLATION_OVER_DATE_LIMIT_CONFIG = (meet, registrationUrl) ->
            new EmailSenderConfig(Template.TOURIST_OVER_DATE_LIMIT_CANCELLATION,
                    ImmutableMap
                            .<String, Object>builder()
                            .put("name", meet.getName())
                            .put("registrationUrl", registrationUrl)
                            .build(),
                    format("%s # %07d", SUBJECT, meet.getId()));

    public static final BiFunction<Meet, String, EmailSenderConfig> TOURIST_CANCELLATION_NOT_SELECTED_CONFIG = (meet, registrationUrl) ->
        new EmailSenderConfig(Template.TOURIST_CANCELLATION_NOT_SELECTED,
                ImmutableMap
                .<String, Object>builder()
                .put("name", meet.getName())
                .put("registrationUrl", registrationUrl)
                .build(),
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
        TOURIST_CANCELLATION(MAIL_TEMPLATE_PATH + "/tourist-cancellation.vm"),
        TOURIST_OVER_DATE_LIMIT_CANCELLATION(MAIL_TEMPLATE_PATH + "/tourist-cancellation-over-meet-limit.vm"),
        TOURIST_CANCELLATION_NOT_SELECTED(MAIL_TEMPLATE_PATH + "/tourist-cancellation-not-selected.vm"),
        VOLUNTEER_CANCELLATION(MAIL_TEMPLATE_PATH + "/volunteer-cancellation.vm"),
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