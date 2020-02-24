package lt.govilnius;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.MeetService;
import lt.govilnius.facadeService.reservation.VolunteerActionService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VolunteerActionServiceTest {

    @Mock
    private MeetEngagementService meetEngagementService;

    @Mock
    private MeetService meetService;

    @InjectMocks
    private VolunteerActionService volunteerActionService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(volunteerActionService, "sentVolunteerRequestWaiting", 500L, Long.class);
        ReflectionTestUtils.setField(volunteerActionService, "evaluationWaiting", 500L, Long.class);
    }

    @Test
    public void agree_Token_ShouldAgree() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setFreezed(false);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        MeetEngagement result = new MeetEngagement(meet, sampleVolunteer(), time, token, true);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        when(meetEngagementService.edit(any(), any())).thenReturn(Optional.of(result));
        MeetEngagement actual = volunteerActionService.agree(token).get();
        Assert.assertEquals(result.getToken(), actual.getToken());
        Assert.assertEquals(result.getTime(), actual.getTime());
        Assert.assertTrue(result.getConfirmed());
        Assert.assertEquals(result.getId(), actual.getId());
    }

    @Test
    public void agree_TokenAndBadStatus_ShouldAgree() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.NEW);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<MeetEngagement> actual = volunteerActionService.agree(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void agree_TokenAndBadTime_ShouldAgree() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - 550L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<MeetEngagement> actual = volunteerActionService.agree(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void agree_Token_ShouldFailEdit() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        MeetEngagement result = new MeetEngagement(meet, sampleVolunteer(), time, token, true);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        when(meetEngagementService.edit(engagement.getId(), engagement)).thenReturn(Optional.ofNullable(null));
        Optional<MeetEngagement> actual = volunteerActionService.agree(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void cancel_Token_ShouldCancel() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        MeetEngagement result = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        when(meetEngagementService.edit(engagement.getId(), engagement)).thenReturn(Optional.of(result));
        MeetEngagement actual = volunteerActionService.cancel(token).get();
        Assert.assertEquals(result.getToken(), actual.getToken());
        Assert.assertFalse(result.getConfirmed());
        Assert.assertEquals(result.getTime(), actual.getTime());
        Assert.assertEquals(result.getId(), actual.getId());
    }

    @Test
    public void cancel_TokenAndBadStatus_ShouldAgree() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.NEW);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<MeetEngagement> actual = volunteerActionService.cancel(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void cancel_TokenAndBadTime_ShouldAgree() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - 550L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<MeetEngagement> actual = volunteerActionService.cancel(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void cancel_Token_ShouldFailEdit() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        MeetEngagement result = new MeetEngagement(meet, sampleVolunteer(), time, token, true);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        when(meetEngagementService.edit(engagement.getId(), engagement)).thenReturn(Optional.ofNullable(null));
        Optional<MeetEngagement> actual = volunteerActionService.cancel(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void editEngagement_Token_ShouldChange() {
        String token = "A";
        String timeString = "20:20:20";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        MeetEngagement result = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        when(meetEngagementService.edit(engagement.getId(), engagement)).thenReturn(Optional.of(result));
        MeetEngagement actual = volunteerActionService.editEngagement(token, timeString).get();
        Assert.assertEquals(result.getToken(), actual.getToken());
        Assert.assertFalse(result.getConfirmed());
        Assert.assertEquals(result.getTime(), actual.getTime());
        Assert.assertEquals(result.getId(), actual.getId());
    }

    @Test
    public void editEngagement_TokenAndBadStatus_ShouldAgree() {
        String timeString = "20:20:20";
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.NEW);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<MeetEngagement> actual = volunteerActionService.editEngagement(token, timeString);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void editEngagement_TokenAndBadTime_ShouldAgree() {
        String timeString = "20:20:20";
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - 550L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<MeetEngagement> actual = volunteerActionService.editEngagement(token, timeString);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void editEngagement_Token_ShouldFailEdit() {
        String timeString = "20:20:20";
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        MeetEngagement result = new MeetEngagement(meet, sampleVolunteer(), time, token, true);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        when(meetEngagementService.edit(engagement.getId(), engagement)).thenReturn(Optional.ofNullable(null));
        Optional<MeetEngagement> actual = volunteerActionService.editEngagement(token, timeString);
        Assert.assertFalse(actual.isPresent());
    }
}
