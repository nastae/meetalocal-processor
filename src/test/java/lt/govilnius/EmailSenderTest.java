package lt.govilnius;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lt.govilnius.domain.reservation.*;
import lt.govilnius.domainService.mail.EmailSender;
import lt.govilnius.domainService.mail.EmailSenderConfig;
import lt.govilnius.domainService.mail.Mail;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Ignore
public class EmailSenderTest {

    @Autowired
    private EmailSender emailSender;

    private static final String RECEIVER = "meetalocaltest@gmail.com";
    private static final String WEBSITE_URL = "test";

    @Test
    public void send_VolunteerRequest_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.VOLUNTEER_REQUEST_CONFIG.apply(sampleMeet(), "TOKEN", WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_TouristRequest_ShouldSend() {
        Meet meet = sampleMeet();
        Volunteer volunteer = sampleVolunteer();
        final EmailSenderConfig config =
                EmailSenderConfig.TOURIST_REQUEST_CONFIG.apply(
                        meet, ImmutableList.of(new MeetEngagement(meet, volunteer, meet.getTime(), "TOKEN", true, false)), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_TouristCancellation_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.TOURIST_CANCELLATION_CONFIG.apply(sampleMeet(), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_TouristCancellationNotSelected_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.TOURIST_CANCELLATION_NOT_SELECTED_CONFIG.apply(sampleMeet(), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_TouristInformation_ShouldSend() {
        Meet meet = sampleMeet();
        Volunteer volunteer = sampleVolunteer();
        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, meet.getTime(), null, true, false);
        final EmailSenderConfig config =
                EmailSenderConfig.TOURIST_INFORMATION_CONFIG.apply(meet, volunteer, meetEngagement);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_TouristEvaluation_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.TOURIST_EVALUATION_CONFIG.apply(
                        new MeetEngagement(sampleMeet(), sampleVolunteer(), new Time(10, 10, 10), "", true, false), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_VolunteerCancellation_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.VOLUNTEER_CANCELLATION_CONFIG.apply(sampleMeet(), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_VolunteerInformation_ShouldSend() {
        Meet meet = sampleMeet();
        Volunteer volunteer = sampleVolunteer();
        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, meet.getTime(), null, true, false);
        final EmailSenderConfig config =
                EmailSenderConfig.VOLUNTEER_INFORMATION_CONFIG.apply(meet, volunteer, meetEngagement);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_VolunteerEvaluation_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.VOLUNTEER_EVALUATION_CONFIG.apply(new MeetEngagement(sampleMeet(), sampleVolunteer(), new Time(10, 10, 10), "", true, false), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    public static Meet sampleMeet() {
        return new Meet(
                new Timestamp(2019, 1, 1, 1, 1, 1, 1),
                new Timestamp(2019, 1, 1, 1, 1, 1, 2),
                "name", "surname", "meetalocaltest@gmail.com",
                "123", "Lithuania", new Date(2019, 11, 11),
                new Time(20, 10, 10),
                1, 20, new HashSet<>(),
                ImmutableSet.<MeetLanguage>builder()
                        .add(new MeetLanguage(Language.ENGLISH, null))
                        .build(),
                "preferences", "additionalPreferences", Status.NEW,
                false, null, new HashSet<>(), new HashSet<>());
    }

    public static Volunteer sampleVolunteer() {
        return sampleVolunteer(Language.RUSSIAN);
    }

    public static Volunteer sampleVolunteer(Language language) {
        Calendar cal = Calendar.getInstance();
        cal.set(1998, 11, 11);
        return new Volunteer(
                new Timestamp(2019, 1, 1, 1, 1, 1, 1),
                new Timestamp(2019, 1, 1, 1, 1, 1, 2),
                "name", "surname", new Date(cal.getTimeInMillis()),
                "123", "meetalocaltest@gmail.com", ImmutableSet.of(sampleVolunteerLanguage(language)),
                "description", true, new HashSet<>());
    }

    public static MeetLanguage sampleMeetLanguage(Language language) {
        return new MeetLanguage(language, null);
    }

    public static VolunteerLanguage sampleVolunteerLanguage(Language language) {
        return new VolunteerLanguage(language, null);
    }
}
