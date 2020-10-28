package lt.govilnius;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.domainService.security.Encryptor;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.repository.reservation.MeetEngagementRepository;
import lt.govilnius.repository.reservation.MeetRepository;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static lt.govilnius.EmailSenderTest.sampleVolunteer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "dev")
public class MeetEngagementServiceTest {

    @Autowired
    private MeetEngagementService meetEngagementService;

    @Autowired
    private MeetEngagementRepository meetEngagementRepository;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private EntityManager manager;

    @Test
    public void create_MeetEngagement_ShouldBeCreated() {
        Volunteer volunteer = volunteerRepository.save(sampleVolunteer());
        Meet meet = meetRepository.save(sampleMeet());
        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();
        Assert.assertEquals(meetEngagement.getMeet().getId(), meet.getId());
        Assert.assertEquals(meetEngagement.getVolunteer().getId(), volunteer.getId());
        Assert.assertEquals(meetEngagement.getTime(), time);
    }

    @Test
    public void getAll_MeetEngagements_ShouldGet() {
        volunteerRepository.findAll().forEach(v -> volunteerRepository.delete(v));
        Volunteer volunteer = volunteerRepository.save(sampleVolunteer());
        Meet meet = meetRepository.save(sampleMeet());
        Time time = new Time(10, 10, 10);
        meetEngagementService.create(meet, volunteer, time);
        List<MeetEngagement> meetEngagements = meetEngagementService.getAll();
        Assert.assertEquals(meetEngagements.size(), 1);
    }

    @Test
    public void getByMeetId_MeetEngagements_ShouldGet() {
        Volunteer volunteer1 = volunteerRepository.save(sampleVolunteer());
        Volunteer volunteer2 = volunteerRepository.save(sampleVolunteer());
        Meet meet = meetRepository.save(sampleMeet());
        Time time = new Time(10, 10, 10);
        meetEngagementService.create(meet, volunteer1, time);
        meetEngagementService.create(meet, volunteer2, time);
        List<MeetEngagement> meetEngagements = meetEngagementService.getByMeetId(meet.getId());
        Assert.assertEquals(meetEngagements.size(), 2);
    }

    @Test
    public void getByVolunteerId_MeetEngagements_ShouldGet() {
        Volunteer volunteer = volunteerRepository.save(sampleVolunteer());
        Meet meet1 = meetRepository.save(sampleMeet());
        Meet meet2 = meetRepository.save(sampleMeet());
        Meet meet3 = meetRepository.save(sampleMeet());
        Time time = new Time(10, 10, 10);
        meetEngagementService.create(meet1, volunteer, time);
        meetEngagementService.create(meet2, volunteer, time);
        meetEngagementService.create(meet3, volunteer, time);
        List<MeetEngagement> meetEngagements = meetEngagementService.getByVolunteerId(volunteer.getId());
        Assert.assertEquals(meetEngagements.size(), 3);
    }

    @Test
    public void findByToken_MeetEngagements_ShouldGet() {
        Volunteer volunteer = volunteerRepository.save(sampleVolunteer());
        Meet meet = meetRepository.save(sampleMeet());
        Time time = new Time(10, 10, 10);
        meetEngagementService.create(meet, volunteer, time);
        meetEngagementService.create(meet, volunteer, time);
        meetEngagementService.create(meet, volunteer, time);
        Optional<MeetEngagement> meetEngagements = meetEngagementService.getByToken(Encryptor.encrypt(meet.getId().toString() + volunteer.getId().toString()));
        Assert.assertTrue(meetEngagements.isPresent());
    }

    @Test
    public void edit_MeetEngagement_ShouldEdit() {
        Volunteer volunteer = volunteerRepository.save(sampleVolunteer());
        Meet meet = meetRepository.save(sampleMeet());
        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();
        Assert.assertEquals(meetEngagement.getConfirmed(), false);
        meetEngagement.setConfirmed(true);
        meetEngagementService.edit(meetEngagement.getId(), meetEngagement);
        Assert.assertEquals(meetEngagement.getConfirmed(), true);
    }
}
