package lt.govilnius;

import com.google.common.collect.ImmutableList;
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
import java.util.HashSet;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Ignore
public class EmailSenderTest {

    @Autowired
    private EmailSender emailSender;

    private static final String RECEIVER = "aurisgo1998@gmail.com";
    private static final String WEBSITE_URL = "test";

    @Test
    public void send_VolunteerRequest_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.VOLUNTEER_REQUEST_CONFIG.apply(sampleMeet(), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_TouristRequest_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.TOURIST_REQUEST_CONFIG.apply(sampleMeet(), ImmutableList.of(sampleVolunteer()), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_VolunteerAddition_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.VOLUNTEER_ADDITION_CONFIG.apply(sampleMeet(), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_TouristInformation_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.TOURIST_INFORMATION_CONFIG.apply(sampleMeet(), sampleVolunteer());
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_VolunteerInformation_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.VOLUNTEER_INFORMATION_CONFIG.apply(sampleMeet(), sampleVolunteer());
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_VolunteerEvaluation_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.VOLUNTEER_EVALUATION_CONFIG.apply(sampleMeet(), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_TouristEvaluation_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.TOURIST_EVALUATION_CONFIG.apply(sampleMeet(), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_Cancellation_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.CANCELLATION_CONFIG.apply(sampleMeet(), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    @Test
    public void send_Report_ShouldSend() {
        final EmailSenderConfig config =
                EmailSenderConfig.REPORT_CONFIG.apply(sampleMeet(), WEBSITE_URL);
        emailSender.send(new Mail(RECEIVER), config);
    }

    public static Meet sampleMeet() {
        return new Meet(
                new Timestamp(2019, 1, 1, 1, 1, 1, 1),
                new Timestamp(2019, 1, 1, 1, 1, 1, 2),
                "meetalocaltest@gmail.com", "123000", "Test", "Test",
                "Vilnius", new Date(2019, 2, 2), new Time(12, 12, 26),
                1, 26, Gender.MALE,
                AgeGroup.YOUTH, new HashSet<>(), "none",
                "comments", Status.NEW, null, new HashSet<>(), new HashSet<>());
    }

    public static Volunteer sampleVolunteer() {
        return sampleVolunteer(Gender.MALE, 26);
    }

    public static Volunteer sampleVolunteer(Gender gender, Integer age) {
        return new Volunteer(
                new Timestamp(2019, 1, 1, 1, 1, 1, 1),
                new Timestamp(2019, 1, 1, 1, 1, 1, 2),
                "Test", "Test", new Date(2000, 1, 1),
                "2312345","meetalocaltest@gmail.com", null,
                "Spain", age, gender,
                "none", true, new HashSet<>(), new HashSet<>());
    }
}
