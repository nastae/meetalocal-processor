package lt.govilnius;

import lt.govilnius.domain.reservation.LiveMeet;
import lt.govilnius.facadeService.reservation.*;
import lt.govilnius.repository.reservation.LiveMeetRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static lt.govilnius.LiveEmailSenderTest.sampleLiveMeet;
import static lt.govilnius.MeetServiceTest.sampleLiveMeetDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(profiles = "dev")
public class LiveMeetServiceTest {

    @Mock
    private LiveMeetRepository meetRepository;

    @Mock
    private MeetStatusService meetStatusService;

    @Mock
    private MeetLanguageService meetLanguageService;

    @Mock
    private MeetAgeGroupService meetAgeGroupService;

    @InjectMocks
    private LiveMeetService liveMeetService;

    @Test
    public void create_Meet_ShouldBeCreated() {
        LiveMeet meet = sampleLiveMeet();
        meet.setId(1L);
        when(meetRepository.save(any())).thenReturn(meet);
        when(meetAgeGroupService.create(any(), any())).thenReturn(Optional.empty());
        when(meetLanguageService.create(any(), any())).thenReturn(Optional.empty());
        when(meetStatusService.create(any(), any())).thenReturn(Optional.empty());
        when(meetRepository.findById(meet.getId())).thenReturn(Optional.of(meet));
        Optional<LiveMeet> result = liveMeetService.create(sampleLiveMeetDto());
        Assert.assertTrue(result.isPresent());
    }
}
