package lt.govilnius.domainService.mail;

import lt.govilnius.domain.reservation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailSenderConfigFactory {

    public EmailSenderConfig getVolunteerRequestConfig(Meet meet, String token, String websiteUrl) {
        return meet.getType().equals(MeetType.Name.LIVE)
                ? LiveEmailSenderConfig.VOLUNTEER_REQUEST_CONFIG.apply((LiveMeet) meet, token, websiteUrl) :
                OnlineEmailSenderConfig.VOLUNTEER_REQUEST_CONFIG.apply((OnlineMeet) meet, token, websiteUrl);
    }

    public EmailSenderConfig getLocalRequestConfig(Meet meet, List<MeetEngagement> meetEngagements, String websiteUrl) {
        return meet.getType().equals(MeetType.Name.LIVE)
                ? LiveEmailSenderConfig.TOURIST_REQUEST_CONFIG.apply((LiveMeet) meet, meetEngagements, websiteUrl) :
                OnlineEmailSenderConfig.TOURIST_REQUEST_CONFIG.apply((OnlineMeet) meet, meetEngagements, websiteUrl);
    }

    public EmailSenderConfig getLocalCancellationConfig(Meet meet, String registrationUrl) {
        return meet.getType().equals(MeetType.Name.LIVE)
                ? LiveEmailSenderConfig.TOURIST_CANCELLATION_CONFIG.apply((LiveMeet) meet, registrationUrl) :
                OnlineEmailSenderConfig.TOURIST_CANCELLATION_CONFIG.apply((OnlineMeet) meet, registrationUrl);
    }

    public EmailSenderConfig getLocalNotValidDateCancellationConfig(Meet meet, String registrationUrl) {
        return meet.getType().equals(MeetType.Name.LIVE)
                ? LiveEmailSenderConfig.TOURIST_CANCELLATION_NOT_VALID_MEET_DATE_CONFIG.apply((LiveMeet) meet, registrationUrl) :
                OnlineEmailSenderConfig.TOURIST_CANCELLATION_NOT_VALID_MEET_DATE_CONFIG.apply((OnlineMeet) meet, registrationUrl);
    }

    public EmailSenderConfig getLocalCancellationNotSelectedConfig(Meet meet, String registrationUrl) {
        return meet.getType().equals(MeetType.Name.LIVE)
                ? LiveEmailSenderConfig.TOURIST_CANCELLATION_NOT_SELECTED_CONFIG.apply((LiveMeet) meet, registrationUrl) :
                OnlineEmailSenderConfig.TOURIST_CANCELLATION_NOT_SELECTED_CONFIG.apply((OnlineMeet) meet, registrationUrl);
    }

    public EmailSenderConfig getVolunteerCancellationConfig(Meet meet, String websiteUrl) {
        return meet.getType().equals(MeetType.Name.LIVE)
                ? LiveEmailSenderConfig.VOLUNTEER_CANCELLATION_CONFIG.apply((LiveMeet) meet, websiteUrl) :
                OnlineEmailSenderConfig.VOLUNTEER_CANCELLATION_CONFIG.apply((OnlineMeet) meet, websiteUrl);
    }

    public EmailSenderConfig getLocalEvaluationConfig(Meet meet, MeetEngagement engagement, String websiteUrl) {
        return meet.getType().equals(MeetType.Name.LIVE)
                ? LiveEmailSenderConfig.TOURIST_EVALUATION_CONFIG.apply(engagement, websiteUrl) :
                OnlineEmailSenderConfig.TOURIST_EVALUATION_CONFIG.apply(engagement, websiteUrl);
    }

    public EmailSenderConfig getVolunteerEvaluationConfig(Meet meet, MeetEngagement engagement, String websiteUrl) {
        return meet.getType().equals(MeetType.Name.LIVE)
                ? LiveEmailSenderConfig.VOLUNTEER_EVALUATION_CONFIG.apply(engagement, websiteUrl) :
                OnlineEmailSenderConfig.VOLUNTEER_EVALUATION_CONFIG.apply(engagement, websiteUrl);
    }

    public EmailSenderConfig getLocalInformationConfig(Meet meet, Volunteer volunteer, MeetEngagement engagement) {
        return meet.getType().equals(MeetType.Name.LIVE)
                ? LiveEmailSenderConfig.TOURIST_INFORMATION_CONFIG.apply((LiveMeet) meet, volunteer, engagement) :
                OnlineEmailSenderConfig.TOURIST_INFORMATION_CONFIG.apply((OnlineMeet) meet, volunteer, engagement);
    }

    public EmailSenderConfig getVolunteerInformationConfig(Meet meet, Volunteer volunteer, MeetEngagement engagement) {
        return meet.getType().equals(MeetType.Name.LIVE)
                ? LiveEmailSenderConfig.VOLUNTEER_INFORMATION_CONFIG.apply((LiveMeet) meet, volunteer, engagement) :
                OnlineEmailSenderConfig.VOLUNTEER_INFORMATION_CONFIG.apply((OnlineMeet) meet, volunteer, engagement);
    }
}
