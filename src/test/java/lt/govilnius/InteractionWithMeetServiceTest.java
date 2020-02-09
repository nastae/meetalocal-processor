package lt.govilnius;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.domain.reservation.dto.TokenDto;
import lt.govilnius.facadeService.reservation.InteractionWithMeetService;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Optional;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static lt.govilnius.EmailSenderTest.sampleVolunteer;
import static org.mockito.Mockito.when;

@RunWith(value = MockitoJUnitRunner.class)
public class InteractionWithMeetServiceTest {

    @Mock
    private MeetEngagementService meetEngagementService;

    @InjectMocks
    private InteractionWithMeetService interactionWithMeetService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(interactionWithMeetService, "sentRequestWaiting", 500L);
    }

    @Test
    public void agree_Token_ShouldAgree() {
        TokenDto tokenDto = new TokenDto("A");
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, "A", false);
        engagement.setId(1L);
        MeetEngagement result = new MeetEngagement(meet, sampleVolunteer(), time, "A", true);
        engagement.setId(1L);
        when(meetEngagementService.getByToken("A")).
                thenReturn(Optional.of(engagement));
        when(meetEngagementService.edit(engagement.getId(), engagement)).thenReturn(Optional.of(result));
        MeetEngagement actual = interactionWithMeetService.agree(tokenDto).get();
        Assert.assertEquals(result.getToken(), actual.getToken());
        Assert.assertEquals(result.getTime(), actual.getTime());
        Assert.assertEquals(result.getId(), actual.getId());
    }

    @Test
    public void agree_TokenAndBadStatus_ShouldAgree() {
        TokenDto tokenDto = new TokenDto("A");
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.NEW);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, "A", false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken("A")).
                thenReturn(Optional.of(engagement));
        Optional<MeetEngagement> actual = interactionWithMeetService.agree(tokenDto);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void agree_TokenAndBadTime_ShouldAgree() {
        TokenDto tokenDto = new TokenDto("A");
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - 550L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, "A", false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken("A")).
                thenReturn(Optional.of(engagement));
        Optional<MeetEngagement> actual = interactionWithMeetService.agree(tokenDto);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void agree_Token_ShouldFailEdit() {
        TokenDto tokenDto = new TokenDto("A");
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, "A", false);
        engagement.setId(1L);
        MeetEngagement result = new MeetEngagement(meet, sampleVolunteer(), time, "A", true);
        engagement.setId(1L);
        when(meetEngagementService.getByToken("A")).
                thenReturn(Optional.of(engagement));
        when(meetEngagementService.edit(engagement.getId(), engagement)).thenReturn(Optional.ofNullable(null));
        Optional<MeetEngagement> actual = interactionWithMeetService.agree(tokenDto);
        Assert.assertFalse(actual.isPresent());
    }
}
