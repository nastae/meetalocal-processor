package lt.govilnius;

import lt.govilnius.domain.reservation.LiveMeet;
import lt.govilnius.domain.reservation.OnlineMeet;
import lt.govilnius.facadeService.reservation.*;
import lt.govilnius.repository.reservation.OnlineMeetRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static lt.govilnius.MeetServiceTest.sampleLiveMeetDto;
import static lt.govilnius.MeetServiceTest.sampleOnlineMeetDto;
import static lt.govilnius.OnlineEmailSenderTest.sampleOnlineMeet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(profiles = "dev")
public class OnlineMeetServiceTest {

    @Mock
    private OnlineMeetRepository meetRepository;

    @Mock
    private MeetStatusService meetStatusService;

    @Mock
    private MeetLanguageService meetLanguageService;

    @Mock
    private MeetAgeGroupService meetAgeGroupService;

    @InjectMocks
    private OnlineMeetService onlineMeetService;

    @Test
    public void create_Meet_ShouldBeCreated() {
        OnlineMeet meet = sampleOnlineMeet();
        meet.setId(1L);
        when(meetRepository.save(any())).thenReturn(meet);
        when(meetAgeGroupService.create(any(), any())).thenReturn(Optional.empty());
        when(meetLanguageService.create(any(), any())).thenReturn(Optional.empty());
        when(meetStatusService.create(any(), any())).thenReturn(Optional.empty());
        when(meetRepository.findById(meet.getId())).thenReturn(Optional.of(meet));
        Optional<OnlineMeet> result = onlineMeetService.create(sampleOnlineMeetDto());
        Assert.assertTrue(result.isPresent());
    }
}
