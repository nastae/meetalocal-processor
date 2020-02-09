package lt.govilnius;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.domainService.schedule.MailSendingService;
import lt.govilnius.facadeService.reservation.MeetService;
import lt.govilnius.facadeService.reservation.VolunteerService;
import lt.govilnius.repository.reservation.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.stream.Collectors;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static lt.govilnius.EmailSenderTest.sampleVolunteer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailSendingServiceTest {

    @Autowired
    private MailSendingService mailSendingService;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private MeetLanguageRepository meetLanguageRepository;

    @Autowired
    private MeetService meetService;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private VolunteerLanguageRepository volunteerLanguageRepository;

    @Autowired
    private MeetEngagementRepository meetEngagementRepository;

    @Autowired
    private ReportRepository reportRepository;

    @After
    public void cleanEachTest() {
        meetRepository.findAll().forEach(meet -> meetRepository.delete(meet));
        volunteerRepository.findAll().forEach(volunteer -> volunteerRepository.delete(volunteer));
        meetEngagementRepository.findAll().forEach(meetEngagement -> meetEngagementRepository.delete(meetEngagement));
        volunteerLanguageRepository.findAll().forEach(volunteerLanguage -> volunteerLanguageRepository.delete(volunteerLanguage));
        meetLanguageRepository.findAll().forEach(meetLanguage -> meetLanguageRepository.delete(meetLanguage));
    }

    @Value("${waiting.sent.request.milliseconds}")
    private Long sentRequestWaiting;

    @Value("${waiting.additional.milliseconds}")
    private Long additionalWaiting;

    @Value("${waiting.responses.milliseconds}")
    private Long responsesWaiting;

    @Value("${waiting.evaluation.milliseconds}")
    private Long evaluationWaiting;

    @Test
    public void processNews_NewMeet_ShouldSetNewStatusAndSend() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.NEW);
        meetService.create(meet);

        Volunteer volunteer = sampleVolunteer();
        volunteer.setAge(meet.getAgeGroup().getFrom());
        volunteer.setLanguages(meet.getLanguages()
                .stream()
                .map(meetLanguage -> new VolunteerLanguage(meetLanguage.getLanguage(),  null))
                .collect(Collectors.toSet()));
        volunteerService.create(volunteer);

        mailSendingService.processNews();
        Assert.assertEquals(meetRepository.findByStatus(Status.NEW).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST).size(), 1);
    }

    @Test
    public void processNews_NewMeetWithoutAvailableVolunteers_ShouldSetCancelled() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.NEW);
        meetRepository.save(meet);

        mailSendingService.processNews();
        Assert.assertEquals(meetRepository.findByStatus(Status.NEW).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.CANCELLATION).size(), 1);
    }

    @Test
    public void processRequests_SentMeetToVolunteer_ShouldSendResponseToTourist() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
                meet.setChangedAt(new Timestamp(System.currentTimeMillis() - sentRequestWaiting));
        meet = meetRepository.save(meet);

        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, new Time(12, 12, 12), "", false);
        meetEngagementRepository.save(meetEngagement);

        mailSendingService.processRequests();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_TOURIST_REQUEST).size(), 1);
    }

    @Test
    public void processRequests_SentMeetToVolunteer_ShouldSendReport() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - sentRequestWaiting));
        meet = meetRepository.save(meet);

        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        Report report = new Report(meet, volunteer, "Comment");
        reportRepository.save(report);

        mailSendingService.processRequests();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.REPORTED).size(), 1);
    }

    @Test
    public void processRequests_SentMeetToVolunteer_ShouldSendAddition() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - sentRequestWaiting));
        meetRepository.save(meet);

        mailSendingService.processRequests();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_TOURIST_ADDITION).size(), 1);
    }

    @Test
    public void processRequests_SentMeetToVolunteerAndBadChangedAt_ShouldDoNothing() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10));
        meetRepository.save(meet);

        mailSendingService.processRequests();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST).size(), 1);
    }

    @Test
    public void processRequestsAfterAdditional_SentMeetToVolunteer_ShouldSendResponseToTourist() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - sentRequestWaiting));
        meet = meetRepository.save(meet);

        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, new Time(12, 12, 12), "", false);
        meetEngagementRepository.save(meetEngagement);

        mailSendingService.processRequestsAfterAdditional();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_TOURIST_REQUEST).size(), 1);
    }

    @Test
    public void processRequestsAfterAdditional_SentMeetToVolunteer_ShouldSendReport() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - sentRequestWaiting));
        meet = meetRepository.save(meet);

        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        Report report = new Report(meet, volunteer, "Comment");
        reportRepository.save(report);

        mailSendingService.processRequestsAfterAdditional();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.REPORTED).size(), 1);
    }

    @Test
    public void processRequestsAfterAdditional_SentMeetToVolunteer_ShouldNotFindVolunteers() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - sentRequestWaiting));
        meetRepository.save(meet);

        mailSendingService.processRequestsAfterAdditional();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.CANCELLATION).size(), 1);
    }

    @Test
    public void processRequestsAfterAdditional_SentMeetToVolunteerAndBadChangedAt_ShouldDoNothing() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10));
        meetRepository.save(meet);

        mailSendingService.processRequestsAfterAdditional();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION).size(), 1);
    }

    @Test
    public void processAdditionals_MeetWithoutEngagements_ShouldSendRequestToTourist() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_TOURIST_ADDITION);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - additionalWaiting));
        meetRepository.save(meet);

        mailSendingService.processAdditionals();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_TOURIST_ADDITION).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION).size(), 1);
    }

    @Test
    public void processAdditionals_MeetWithoutEngagementsAndWithBadChangedAt_ShouldDoNothing() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_TOURIST_ADDITION);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10));
        meetRepository.save(meet);

        mailSendingService.processAdditionals();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_TOURIST_ADDITION).size(), 1);
    }

    @Test
    public void processResponses_SentMeetToTourist_ShouldSendAgreement() {
        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - responsesWaiting));
        meet.setVolunteer(volunteer);
        meetRepository.save(meet);

        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, new Time(12, 12, 12), "", false);
        meetEngagementRepository.save(meetEngagement);

        mailSendingService.processResponses();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_TOURIST_REQUEST).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.AGREED).size(), 1);
    }

    @Test
    public void processResponses_SentMeetToTourist_ShouldSendCancellation() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - responsesWaiting));
        meetRepository.save(meet);

        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, new Time(12, 12, 12), "", false);
        meetEngagementRepository.save(meetEngagement);

        mailSendingService.processResponses();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_TOURIST_REQUEST).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.CANCELLATION).size(), 1);
    }

    @Test
    public void processResponses_SentMeetToTouristAndBadChangedAt_ShouldDoNothing() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10));
        meetRepository.save(meet);

        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, new Time(12, 12, 12), "", false);
        meetEngagementRepository.save(meetEngagement);

        mailSendingService.processResponses();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_TOURIST_REQUEST).size(), 1);
    }

    @Test
    public void processAgreements_AgreedMeet_ShouldSendEvaluations() {
        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        Meet meet = sampleMeet();
        meet.setStatus(Status.AGREED);
        meet.setVolunteer(volunteer);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - evaluationWaiting));
        meetRepository.save(meet);

        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, new Time(12, 12, 12), "", false);
        meetEngagementRepository.save(meetEngagement);

        mailSendingService.processAgreements();
        Assert.assertEquals(meetRepository.findByStatus(Status.AGREED).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.FINISHED).size(), 1);
    }

    @Test
    public void processAgreements_NotAgreedMeet_ShouldRegisterError() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.AGREED);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - evaluationWaiting));
        meetRepository.save(meet);

        mailSendingService.processAgreements();
        Assert.assertEquals(meetRepository.findByStatus(Status.AGREED).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.ERROR).size(), 1);
    }

    @Test
    public void processAgreements_AgreedMeetAndBadChangedAt_ShouldDoNothing() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.AGREED);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10));
        meetRepository.save(meet);

        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, new Time(12, 12, 12), "", false);
        meetEngagementRepository.save(meetEngagement);

        mailSendingService.processAgreements();
        Assert.assertEquals(meetRepository.findByStatus(Status.AGREED).size(), 1);
    }
}
