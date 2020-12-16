package lt.govilnius.domainService.mail;

import com.google.common.collect.ImmutableMap;
import com.speedment.common.function.TriFunction;
import lt.govilnius.domain.reservation.*;
import lt.govilnius.domainService.encode.HTMLSymbolEncoderUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class LiveEmailSenderConfig {

    private static final String MAIL_TEMPLATE_PATH = "mails/live";
    private static final String SUBJECT = "Meet a Local Live Confirmation";

    public static final TriFunction<LiveMeet, String, String, EmailSenderConfig> VOLUNTEER_REQUEST_CONFIG = (meet, token, websiteUrl) -> {
        Calendar cal = Calendar.getInstance();
        cal.setTime(meet.getDate());
        Calendar time = Calendar.getInstance();
        time.setTime(meet.getTime());
        String languages = meet.getLanguages()
                .stream()
                .map(l -> l.getLanguage().getName())
                .collect(Collectors.joining(", "));
        return new EmailSenderConfig(Template.VOLUNTEER_REQUEST.getPath(), ImmutableMap
                .<String, Object>builder()
                .put("month", HTMLSymbolEncoderUtils.encode(DateUtils.monthToLithuanian(cal.get(Calendar.MONTH))))
                .put("day", cal.get(Calendar.DAY_OF_MONTH))
                .put("hours", format("%02d", time.get(Calendar.HOUR_OF_DAY)))
                .put("minutes", format("%02d", time.get(Calendar.MINUTE)))
                .put("name", HTMLSymbolEncoderUtils.encode(meet.getName()))
                .put("languages", HTMLSymbolEncoderUtils.encode(languages))
                .put("age", meet.getAge())
                .put("count", meet.getPeopleCount())
                .put("preferences", HTMLSymbolEncoderUtils.encode(meet.getPreferences()))
                .put("additionalPreferences", HTMLSymbolEncoderUtils.encode(meet.getAdditionalPreferences() == null
                        ? ""
                        : meet.getAdditionalPreferences()))
                .put("agreementUrl", websiteUrl + "/volunteer/agreements?token=" + token)
                .put("cancellationUrl", websiteUrl + "/volunteer/cancellations?token=" + token)
                .put("editUrl", websiteUrl + "/volunteer/engagements?token=" + token)
                .build(),
                format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final TriFunction<LiveMeet, List<MeetEngagement>, String, EmailSenderConfig> TOURIST_REQUEST_CONFIG = (meet, engagements, websiteUrl) -> {
        final Map<String, Object> model = new HashMap<>();
        model.put("name", HTMLSymbolEncoderUtils.encode(meet.getName()));
        model.put("engagements", engagements
                .stream()
                .map(e -> {
                    Volunteer volunteer = e.getVolunteer();
                    volunteer.setName(HTMLSymbolEncoderUtils.encode(volunteer.getName()));
                    volunteer.setSurname(HTMLSymbolEncoderUtils.encode(volunteer.getSurname()));
                    volunteer.setEmail(HTMLSymbolEncoderUtils.encode(volunteer.getEmail()));
                    volunteer.setDescription(HTMLSymbolEncoderUtils.encode(volunteer.getDescription()));
                    e.setVolunteer(volunteer);
                    return e;
                })
                .filter(e -> e.getTime().equals(meet.getTime()))
                .collect(Collectors.toList()));
        model.put("engagementsWithEditedTime", engagements
                .stream()
                .map(e -> {
                    Volunteer volunteer = e.getVolunteer();
                    volunteer.setName(HTMLSymbolEncoderUtils.encode(volunteer.getName()));
                    volunteer.setSurname(HTMLSymbolEncoderUtils.encode(volunteer.getSurname()));
                    volunteer.setEmail(HTMLSymbolEncoderUtils.encode(volunteer.getEmail()));
                    volunteer.setDescription(HTMLSymbolEncoderUtils.encode(volunteer.getDescription()));
                    e.setVolunteer(volunteer);
                    return e;
                })
                .filter(e -> !(e.getTime().equals(meet.getTime())))
                .collect(Collectors.toList()));
        model.put("websiteUrl", websiteUrl);

        return new EmailSenderConfig(Template.LOCAL_REQUEST.getPath(), model, format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final TriFunction<LiveMeet, Volunteer, MeetEngagement, EmailSenderConfig> TOURIST_INFORMATION_CONFIG = (meet, v, e) -> {
        Calendar date = Calendar.getInstance();
        date.setTime(meet.getDate());
        Calendar time = Calendar.getInstance();
        time.setTime(e.getTime());
        return new EmailSenderConfig(Template.LOCAL_INFORMATION.getPath(), ImmutableMap
                .<String, Object>builder()
                .put("name", HTMLSymbolEncoderUtils.encode(meet.getName()))
                .put("month", DateUtils.monthToEnglish(date.get(Calendar.MONTH)))
                .put("day", String.valueOf(date.get(Calendar.DAY_OF_MONTH)))
                .put("hours", format("%02d", time.get(Calendar.HOUR_OF_DAY)))
                .put("minutes", format("%02d", time.get(Calendar.MINUTE)))
                .put("friendName", HTMLSymbolEncoderUtils.encode(v.getName()))
                .put("phoneNumber", v.getPhoneNumber())
                .put("email", HTMLSymbolEncoderUtils.encode(v.getEmail()))
                .build(),
                format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final TriFunction<LiveMeet, Volunteer, MeetEngagement, EmailSenderConfig> VOLUNTEER_INFORMATION_CONFIG = (meet, v, e) -> {
        Calendar date = Calendar.getInstance();
        date.setTime(meet.getDate());
        Calendar time = Calendar.getInstance();
        time.setTime(e.getTime());
        Template template = Template.VOLUNTEER_TOURISM_INFORMATION;
        if (meet.getPurpose().equals(Purpose.RELOCATION)) {
            template = Template.VOLUNTEER_RELOCATION_INFORMATION;
        }
        return new EmailSenderConfig(template.getPath(), ImmutableMap
                .<String, Object>builder()
                .put("month", HTMLSymbolEncoderUtils.encode(DateUtils.monthToLithuanian(date.get(Calendar.MONTH))))
                .put("day", String.valueOf(date.get(Calendar.DAY_OF_MONTH)))
                .put("hours", format("%02d", time.get(Calendar.HOUR_OF_DAY)))
                .put("minutes", format("%02d", time.get(Calendar.MINUTE)))
                .put("name", HTMLSymbolEncoderUtils.encode(v.getName()))
                .put("phoneNumber", meet.getPhoneNumber())
                .put("email", HTMLSymbolEncoderUtils.encode(meet.getEmail()))
                .build(),
                format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final BiFunction<MeetEngagement, String, EmailSenderConfig> TOURIST_EVALUATION_CONFIG = (engagement, websiteUrl) -> {
        final LiveMeet meet = (LiveMeet) engagement.getMeet();
        return new EmailSenderConfig(Template.LOCAL_EVALUATION.getPath(), ImmutableMap
                .<String, Object>builder()
                .put("name", HTMLSymbolEncoderUtils.encode(meet.getName()))
                .put("evaluationUrl", websiteUrl + "/tourist/evaluations?token=" + engagement.getToken())
                .build(),
                format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final BiFunction<MeetEngagement, String, EmailSenderConfig> VOLUNTEER_EVALUATION_CONFIG = (engagement, websiteUrl) -> {
        final LiveMeet meet = (LiveMeet) engagement.getMeet();
        Template template = Template.VOLUNTEER_TOURISM_EVALUATION;
        if (meet.getPurpose().equals(Purpose.RELOCATION)) {
            template = Template.VOLUNTEER_RELOCATION_EVALUATION;
        }
        return new EmailSenderConfig(template.getPath(), ImmutableMap
                .<String, Object>builder()
                .put("evaluationUrl", websiteUrl + "/volunteer/evaluations?token=" + engagement.getToken())
                .build(),
                format("%s # %07d", SUBJECT, meet.getId()));
    };

    public static final BiFunction<Meet, String, EmailSenderConfig> VOLUNTEER_CANCELLATION_CONFIG = (meet, websiteUrl) ->
            new EmailSenderConfig(Template.VOLUNTEER_CANCELLATION.getPath(), ImmutableMap.of(),
                    format("%s # %07d", SUBJECT, meet.getId()));

    public static final BiFunction<LiveMeet, String, EmailSenderConfig> TOURIST_CANCELLATION_CONFIG = (meet, registrationUrl) ->
            new EmailSenderConfig(Template.LOCAL_CANCELLATION.getPath(),
                    ImmutableMap
                            .<String, Object>builder()
                            .put("name", HTMLSymbolEncoderUtils.encode(meet.getName()))
                            .put("registrationUrl", registrationUrl)
                            .build(),
                    format("%s # %07d", SUBJECT, meet.getId()));

    public static final BiFunction<LiveMeet, String, EmailSenderConfig> TOURIST_CANCELLATION_NOT_VALID_MEET_DATE_CONFIG = (meet, registrationUrl) ->
            new EmailSenderConfig(Template.LOCAL_NOT_VALID_MEET_DATE_CANCELLATION.getPath(),
                    ImmutableMap
                            .<String, Object>builder()
                            .put("name", HTMLSymbolEncoderUtils.encode(meet.getName()))
                            .put("registrationUrl", registrationUrl)
                            .build(),
                    format("%s # %07d", SUBJECT, meet.getId()));

    public static final BiFunction<LiveMeet, String, EmailSenderConfig> TOURIST_CANCELLATION_NOT_SELECTED_CONFIG = (meet, registrationUrl) ->
            new EmailSenderConfig(Template.LOCAL_CANCELLATION_NOT_SELECTED.getPath(),
                    ImmutableMap
                            .<String, Object>builder()
                            .put("name", HTMLSymbolEncoderUtils.encode(meet.getName()))
                            .put("registrationUrl", registrationUrl)
                            .build(),
                    format("%s # %07d", SUBJECT, meet.getId()));

    /**
     * @Template - E-mail template
     */
    public enum Template {
        VOLUNTEER_REQUEST(MAIL_TEMPLATE_PATH + "/volunteer-request.vm"),
        LOCAL_REQUEST(MAIL_TEMPLATE_PATH + "/local-request.vm"),
        LOCAL_CANCELLATION(MAIL_TEMPLATE_PATH + "/local-cancellation.vm"),
        LOCAL_NOT_VALID_MEET_DATE_CANCELLATION(MAIL_TEMPLATE_PATH + "/local-cancellation-not-valid-meet-date.vm"),
        LOCAL_CANCELLATION_NOT_SELECTED(MAIL_TEMPLATE_PATH + "/local-cancellation-not-selected.vm"),
        VOLUNTEER_CANCELLATION(MAIL_TEMPLATE_PATH + "/volunteer-cancellation.vm"),
        LOCAL_EVALUATION(MAIL_TEMPLATE_PATH + "/local-evaluation.vm"),

        VOLUNTEER_RELOCATION_EVALUATION(MAIL_TEMPLATE_PATH + "/relocation/volunteer-evaluation.vm"),
        VOLUNTEER_TOURISM_EVALUATION(MAIL_TEMPLATE_PATH + "/tourism/volunteer-evaluation.vm"),

        LOCAL_INFORMATION(MAIL_TEMPLATE_PATH + "/local-information.vm"),

        VOLUNTEER_RELOCATION_INFORMATION(MAIL_TEMPLATE_PATH + "/relocation/volunteer-information.vm"),
        VOLUNTEER_TOURISM_INFORMATION(MAIL_TEMPLATE_PATH + "/tourism/volunteer-information.vm");

        public final String path;

        private Template(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
