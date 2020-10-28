package lt.govilnius;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.domainService.schedule.TouristMailProcessor;
import lt.govilnius.facadeService.reservation.MeetService;
import lt.govilnius.facadeService.reservation.VolunteerActionService;
import lt.govilnius.repository.reservation.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.sql.Time;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static lt.govilnius.EmailSenderTest.sampleVolunteer;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "dev")
public class TouristMailProcessorTest {

    @Autowired
    private TouristMailProcessor touristMailProcessor;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private MeetLanguageRepository meetLanguageRepository;

    @Autowired
    private MeetService meetService;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private VolunteerLanguageRepository volunteerLanguageRepository;

    @Autowired
    private VolunteerPurposeRepository volunteerPurposeRepository;

    @Autowired
    private VolunteerActionService volunteerActionService;

    @Autowired
    private MeetEngagementRepository meetEngagementRepository;

    @Autowired
    private MeetStatusRepository meetStatusRepository;

    @Autowired
    private MeetAgeGroupRepository meetAgeGroupRepository;

    @Autowired
    private EntityManager manager;

    @After
    public void cleanEachTest() {
        manager.clear();
    }

    @Test
    @Ignore
    public void processTouristRequest_SentMeetToTourist_ShouldSendAgreement() {
        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_LOCAL_REQUEST);
        meet.setVolunteer(volunteer);
        meetRepository.save(meet);

        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, new Time(12, 12, 12), "", false, false);
        meetEngagement = meetEngagementRepository.save(meetEngagement);

        touristMailProcessor.processRequest(meetEngagement.getVolunteer(), meetEngagement.getMeet());
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_LOCAL_REQUEST).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.AGREED).size(), 1);
    }

    @Test
    @Ignore
    public void processTouristRequest_SentMeetToTouristWithoutEngagement_ShouldSendCancellation() {
        Volunteer volunteer = sampleVolunteer();
        volunteer = volunteerRepository.save(volunteer);

        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_LOCAL_REQUEST);
        meet.setVolunteer(volunteer);
        meet = meetRepository.save(meet);

        touristMailProcessor.processRequest(volunteer, meet);
        Assert.assertEquals(meetRepository.findAll().size(), 1);
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_LOCAL_REQUEST).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.CANCELED).size(), 1);
    }
}
