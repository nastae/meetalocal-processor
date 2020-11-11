package lt.govilnius;

import com.google.common.collect.ImmutableList;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetStatus;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.facadeService.reservation.MeetStatusService;
import lt.govilnius.repository.reservation.MeetStatusRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.util.List;

import static lt.govilnius.LiveEmailSenderTest.sampleLiveMeet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(profiles = "dev")
public class MeetStatusServiceTest {

    @Mock
    private MeetStatusRepository repository;

    @InjectMocks
    private MeetStatusService meetStatusService;

    @Test
    public void create_Status_ShouldBeCreated() {
        Meet meet = sampleLiveMeet();
        MeetStatus status = new MeetStatus(new Timestamp(100L), meet, Status.NEW);
        when(repository.save(any())).thenReturn(status);
        MeetStatus result = meetStatusService.create(meet, Status.NEW).get();
        Assert.assertEquals(result.getMeet().getId(), meet.getId());
        Assert.assertEquals(result.getStatus(), Status.NEW);
    }

    @Test
    public void getByMeetId_Statuses_ShouldGet() {
        Meet meet = sampleLiveMeet();
        meet.setId(1L);
        MeetStatus status = new MeetStatus(new Timestamp(100L), meet, Status.NEW);
        when(repository.findByMeetId(meet.getId())).thenReturn(ImmutableList.of(status, status));
        List<MeetStatus> statuses = meetStatusService.getByMeetId(meet.getId());
        Assert.assertEquals(statuses.size(), 2);
    }
}
