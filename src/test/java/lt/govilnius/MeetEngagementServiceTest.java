package lt.govilnius;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.repository.reservation.MeetEngagementRepository;
import lt.govilnius.repository.reservation.MeetRepository;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static lt.govilnius.EmailSenderTest.sampleVolunteer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MeetEngagementServiceTest {

    @Autowired
    private MeetEngagementService meetEngagementService;

    @Autowired
    private MeetEngagementRepository meetEngagementRepository;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @After
    public void cleanEachTest() {
        meetEngagementRepository.findAll().forEach(volunteer -> meetEngagementRepository.delete(volunteer));
        meetRepository.findAll().forEach(meet -> meetRepository.delete(meet));
        volunteerRepository.findAll().forEach(volunteer -> volunteerRepository.delete(volunteer));
    }

    @Test
    public void create_Volunteer_ShouldBeCreated() {
        Volunteer volunteer = volunteerRepository.save(sampleVolunteer());
        Meet meet = meetRepository.save(sampleMeet());
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer).right().get();
        Assert.assertEquals(meetEngagement.getMeet().getId(), meet.getId());
        Assert.assertEquals(meetEngagement.getVolunteer().getId(), volunteer.getId());
    }

    @Test
    public void getAll_MeetEngagements_ShouldGet() {
        Volunteer volunteer = volunteerRepository.save(sampleVolunteer());
        Meet meet = meetRepository.save(sampleMeet());
        meetEngagementService.create(meet, volunteer);
        List<MeetEngagement> meetEngagements = meetEngagementService.getAll();
        Assert.assertEquals(meetEngagements.size(), 1);
    }

    @Test
    public void getByMeetId_MeetEngagements_ShouldGet() {
        Volunteer volunteer = volunteerRepository.save(sampleVolunteer());
        Meet meet = meetRepository.save(sampleMeet());
        meetEngagementService.create(meet, volunteer);
        meetEngagementService.create(meet, volunteer);
        List<MeetEngagement> meetEngagements = meetEngagementService.getByMeetId(meet.getId());
        Assert.assertEquals(meetEngagements.size(), 2);
    }

    @Test
    public void getByVolunteerId_MeetEngagements_ShouldGet() {
        Volunteer volunteer = volunteerRepository.save(sampleVolunteer());
        Meet meet = meetRepository.save(sampleMeet());
        meetEngagementService.create(meet, volunteer);
        meetEngagementService.create(meet, volunteer);
        meetEngagementService.create(meet, volunteer);
        List<MeetEngagement> meetEngagements = meetEngagementService.getByMeetId(meet.getId());
        Assert.assertEquals(meetEngagements.size(), 3);
    }
}
