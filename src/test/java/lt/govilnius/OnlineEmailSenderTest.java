package lt.govilnius;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lt.govilnius.domain.reservation.*;
import lt.govilnius.domainService.mail.EmailSender;
import lt.govilnius.domainService.mail.EmailSenderConfig;
import lt.govilnius.domainService.mail.EmailSenderConfigFactory;
import lt.govilnius.domainService.mail.Mail;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Ignore
@ActiveProfiles(profiles = "dev")
public class OnlineEmailSenderTest {


    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailSenderConfigFactory configFactory;

    private static final String RECEIVER = "meetalocaltest@gmail.com";
    private static final String WEBSITE_URL = "test";

    @Test
    public void send_VolunteerRequest_ShouldSend() {
        final EmailSenderConfig config = configFactory.getVolunteerRequestConfig(sampleOnlineMeet(), "TOKEN", WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_TouristRequest_ShouldSend() {
        OnlineMeet meet = sampleOnlineMeet();
        Volunteer volunteer = sampleVolunteer();
        final EmailSenderConfig config = configFactory.getLocalRequestConfig(meet, ImmutableList.of(
                new MeetEngagement(meet, volunteer, meet.getTime(), "TOKEN", true, false)), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_TouristRequest_With_Another_Time_ShouldSend() {
        OnlineMeet meet = sampleOnlineMeet();
        Volunteer volunteer = sampleVolunteer();
        final EmailSenderConfig config = configFactory.getLocalRequestConfig(meet, ImmutableList.of(
                new MeetEngagement(meet, volunteer, meet.getTime(), "TOKEN", true, false),
                new MeetEngagement(meet, volunteer, new Time(5, 5, 5), "TOKEN", true, false)), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_TouristCancellation_ShouldSend() {
        final EmailSenderConfig config = configFactory.getLocalCancellationConfig(sampleOnlineMeet(), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_TouristCancellationNotSelected_ShouldSend() {
        final EmailSenderConfig config = configFactory.getLocalCancellationNotSelectedConfig(sampleOnlineMeet(), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_TouristInformation_ShouldSend() {
        OnlineMeet meet = sampleOnlineMeet();
        Volunteer volunteer = sampleVolunteer();
        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, meet.getTime(), null, true, false);
        final EmailSenderConfig config = configFactory.getLocalInformationConfig(meet, volunteer, meetEngagement);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_TouristEvaluation_ShouldSend() {
        OnlineMeet meet = sampleOnlineMeet();
        final EmailSenderConfig config = configFactory.getLocalEvaluationConfig(meet, new MeetEngagement(meet, sampleVolunteer(), new Time(10, 10, 10), "", true, false), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_VolunteerCancellation_ShouldSend() {
        final EmailSenderConfig config = configFactory.getVolunteerCancellationConfig(sampleOnlineMeet(), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_VolunteerInformation_ShouldSend() {
        OnlineMeet meet = sampleOnlineMeet();
        Volunteer volunteer = sampleVolunteer();
        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, meet.getTime(), null, true, false);
        final EmailSenderConfig config = configFactory.getVolunteerInformationConfig(meet, volunteer, meetEngagement);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_VolunteerRelocationEvaluation_ShouldSend() {
        OnlineMeet meet = sampleOnlineMeet();
        meet.setPurpose(Purpose.RELOCATION);
        final EmailSenderConfig config = configFactory.getVolunteerEvaluationConfig(meet, new MeetEngagement(meet, sampleVolunteer(), new Time(10, 10, 10), "", true, false), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_VolunteerTourivsmEvaluation_ShouldSend() {
        OnlineMeet meet = sampleOnlineMeet();
        meet.setPurpose(Purpose.TOURISM);
        final EmailSenderConfig config = configFactory.getVolunteerEvaluationConfig(meet, new MeetEngagement(meet, sampleVolunteer(), new Time(10, 10, 10), "", true, false), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    public static OnlineMeet sampleOnlineMeet() {
        return new OnlineMeet(
                new Timestamp(2019, 1, 1, 1, 1, 1, 1),
                new Timestamp(2019, 1, 1, 1, 1, 1, 2),
                "name", "surname", "meetalocaltest@gmail.com", Purpose.RELOCATION,
                "Lithuania" , new Date(2025, 11, 11),
                new Time(20, 10, 10), "19-20", "123", "123",
                new HashSet<>(),
                ImmutableSet.<MeetLanguage>builder()
                        .add(new MeetLanguage(Language.ENGLISH, null))
                        .build(),
                new HashSet<>(), new HashSet<>(), sampleVolunteer(), Status.NEW, false,
                "12313");
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
                "name", "surname", new Date(cal.getTimeInMillis()), "name",
                "123", "meetalocaltest@gmail.com", new HashSet<>(),
                "description", true, new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public static MeetLanguage sampleMeetLanguage(Language language) {
        return new MeetLanguage(language, null);
    }

    public static VolunteerLanguage sampleVolunteerLanguage(Language language) {
        return new VolunteerLanguage(language, null);
    }
}
