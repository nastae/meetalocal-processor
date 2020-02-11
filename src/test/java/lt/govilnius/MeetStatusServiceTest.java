package lt.govilnius;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetStatus;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.facadeService.reservation.MeetStatusService;
import lt.govilnius.repository.reservation.MeetRepository;
import lt.govilnius.repository.reservation.MeetStatusRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static lt.govilnius.EmailSenderTest.sampleMeet;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MeetStatusServiceTest {

    @Autowired
    private MeetStatusService meetStatusService;

    @Autowired
    private MeetStatusRepository meetStatusRepository;

    @Autowired
    private MeetRepository meetRepository;

    @After
    public void cleanEachTest() {
        meetStatusRepository.findAll().forEach(status -> meetStatusRepository.delete(status));
        meetRepository.findAll().forEach(meet -> meetRepository.delete(meet));
    }

    @Test
    public void create_Status_ShouldBeCreated() {
        Meet meet = meetRepository.save(sampleMeet());
        MeetStatus status = meetStatusService.create(meet, Status.NEW).get();
        Assert.assertEquals(status.getMeet().getId(), meet.getId());
        Assert.assertEquals(status.getStatus(), Status.NEW);
    }

    @Test
    public void getByMeetId_Statuses_ShouldGet() {
        Meet meet = meetRepository.save(sampleMeet());
        meetStatusService.create(meet, Status.NEW).get();
        meetStatusService.create(meet, Status.SENT_VOLUNTEER_REQUEST).get();
        meetStatusService.create(meet, Status.SENT_TOURIST_REQUEST).get();
        List<MeetStatus> statuses = meetStatusService.getByMeetId(meet.getId());
        Assert.assertEquals(statuses.size(), 3);
    }
}
