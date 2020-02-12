package lt.govilnius;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.domainService.schedule.ProactiveMailSendingService;
import lt.govilnius.facadeService.reservation.InteractionWithMeetService;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.MeetService;
import lt.govilnius.facadeService.reservation.ReportService;
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

@RunWith(value = MockitoJUnitRunner.class)
public class InteractionWithMeetServiceTest {

    @Mock
    private MeetEngagementService meetEngagementService;

    @Mock
    private ReportService reportService;

    @Mock
    private MeetService meetService;

    @Mock
    private ProactiveMailSendingService proactiveMailSendingService;

    @InjectMocks
    private InteractionWithMeetService interactionWithMeetService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(interactionWithMeetService, "sentRequestWaiting", 500L);
    }

    @Test
    public void agree_Token_ShouldAgree() {
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
        when(meetEngagementService.edit(engagement.getId(), engagement)).thenReturn(Optional.of(result));
        MeetEngagement actual = interactionWithMeetService.agree(token).get();
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
        Optional<MeetEngagement> actual = interactionWithMeetService.agree(token);
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
        Optional<MeetEngagement> actual = interactionWithMeetService.agree(token);
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
        Optional<MeetEngagement> actual = interactionWithMeetService.agree(token);
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
        MeetEngagement actual = interactionWithMeetService.cancel(token).get();
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
        Optional<MeetEngagement> actual = interactionWithMeetService.cancel(token);
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
        Optional<MeetEngagement> actual = interactionWithMeetService.cancel(token);
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
        Optional<MeetEngagement> actual = interactionWithMeetService.cancel(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void changeEngagement_Token_ShouldChange() {
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
        MeetEngagement actual = interactionWithMeetService.changeEngagement(token, timeString).get();
        Assert.assertEquals(result.getToken(), actual.getToken());
        Assert.assertFalse(result.getConfirmed());
        Assert.assertEquals(result.getTime(), actual.getTime());
        Assert.assertEquals(result.getId(), actual.getId());
    }

    @Test
    public void changeEngagement_TokenAndBadStatus_ShouldAgree() {
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
        Optional<MeetEngagement> actual = interactionWithMeetService.changeEngagement(token, timeString);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void changeEngagement_TokenAndBadTime_ShouldAgree() {
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
        Optional<MeetEngagement> actual = interactionWithMeetService.changeEngagement(token, timeString);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void changeEngagement_Token_ShouldFailEdit() {
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
        Optional<MeetEngagement> actual = interactionWithMeetService.changeEngagement(token, timeString);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void changeMeetAfterAddition_Token_ShouldChange() {
        String meetId = "1010";
        String timeString = "20:20:20";
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_TOURIST_ADDITION);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        meet.setId(Long.parseLong(meetId));
        Meet result = sampleMeet();
        when(meetService.get(Long.parseLong(meetId))).
                thenReturn(Optional.of(meet));
        when(proactiveMailSendingService.processAddition(any(), any())).thenReturn(Optional.of(result));
        Meet actual = interactionWithMeetService.changeMeetAfterAddition(meetId, timeString).get();
        Assert.assertEquals(result.getTime(), actual.getTime());
        Assert.assertEquals(result.getId(), actual.getId());
    }

    @Test
    public void changeMeetAfterAddition_TokenAndBadStatus_ShouldAgree() {
        String meetId = "1010";
        String timeString = "20:20:20";
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        when(meetService.get(Long.parseLong(meetId))).
                thenReturn(Optional.of(meet));
        Optional<Meet> actual = interactionWithMeetService.changeMeetAfterAddition(meetId, timeString);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void changeMeetAfterAddition_TokenAndBadTime_ShouldAgree() {
        String timeString = "20:20:20";
        String meetId = "1010";
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_TOURIST_ADDITION);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - 550L));
        when(meetService.get(Long.parseLong(meetId))).
                thenReturn(Optional.of(meet));
        when(proactiveMailSendingService.processAddition(any(), any())).thenReturn(Optional.empty());
        Optional<Meet> actual = interactionWithMeetService.changeMeetAfterAddition(meetId, timeString);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void changeMeetAfterAddition_Token_ShouldFailEdit() {
        String timeString = "20:20:20";
        String meetId = "1010";
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_TOURIST_ADDITION);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        when(meetService.get(Long.parseLong(meetId))).
                thenReturn(Optional.of(meet));
        when(proactiveMailSendingService.processAddition(any(), any())).thenReturn(Optional.empty());
        Optional<Meet> actual = interactionWithMeetService.changeMeetAfterAddition(meetId, timeString);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void report_Token_ShouldChange() {
        String token = "A";
        String comment = "Comment";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        Volunteer volunteer = sampleVolunteer();
        MeetEngagement engagement = new MeetEngagement(meet, volunteer, time, token, false);
        engagement.setId(1L);
        Report result = new Report(meet, volunteer, comment);
        result.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        when(reportService.create(meet, volunteer, comment)).thenReturn(Optional.of(result));
        Report actual = interactionWithMeetService.report(token, comment).get();
        Assert.assertEquals(comment, actual.getComment());
        Assert.assertEquals(result.getId(), actual.getId());
    }

    @Test
    public void report_TokenAndBadStatus_ShouldAgree() {
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
        Optional<Report> actual = interactionWithMeetService.report(token, timeString);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void report_TokenAndBadTime_ShouldAgree() {
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
        Optional<Report> actual = interactionWithMeetService.report(token, timeString);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void report_Token_ShouldFailEdit() {
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
        Optional<Report> actual = interactionWithMeetService.report(token, timeString);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void select_Token_ShouldSelect() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        meet.setVolunteer(null);
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, true);
        engagement.setId(1L);
        Meet result = sampleMeet();
        result.setStatus(Status.SENT_TOURIST_REQUEST);
        result.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        result.setVolunteer(sampleVolunteer());
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        when(proactiveMailSendingService.processTouristRequest(any(), any())).thenReturn(Optional.of(result));
        Meet actual = interactionWithMeetService.select(token).get();
        Assert.assertNotNull(actual.getVolunteer());
        Assert.assertEquals(meet.getId(), actual.getId());
    }

    @Test
    public void select_TokenAndBadStatus_ShouldNotSelect() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, true);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<Meet> actual = interactionWithMeetService.select(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void select_TokenAndBadTime_ShouldNotSelect() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - 550L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, true);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<Meet> actual = interactionWithMeetService.select(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void select_TokenAndNotEngaged_ShouldNotSelect() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        meet.setVolunteer(null);
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false);
        engagement.setId(1L);
        Meet result = sampleMeet();
        result.setStatus(Status.SENT_TOURIST_REQUEST);
        result.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        result.setVolunteer(sampleVolunteer());
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<Meet> actual = interactionWithMeetService.select(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void select_Token_ShouldFailEdit() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, true);
        engagement.setId(1L);
        MeetEngagement result = new MeetEngagement(meet, sampleVolunteer(), time, token, true);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<Meet> actual = interactionWithMeetService.select(token);
        Assert.assertFalse(actual.isPresent());
    }
}
