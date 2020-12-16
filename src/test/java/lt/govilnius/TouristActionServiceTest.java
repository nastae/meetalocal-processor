package lt.govilnius;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.domainService.schedule.TouristMailProcessor;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.MeetService;
import lt.govilnius.facadeService.reservation.TouristActionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Optional;

import static lt.govilnius.LiveEmailSenderTest.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(profiles = "dev")
public class TouristActionServiceTest {

    @Mock
    private MeetEngagementService meetEngagementService;

    @Mock
    private MeetService meetService;

    @Mock
    private TouristMailProcessor touristMailProcessor;

    @InjectMocks
    private TouristActionService touristActionService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(touristActionService, "sentVolunteerRequestWaiting", 500L, Long.class);
    }

    @Test
    public void select_Token_ShouldSelect_WithLiveMeet() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleLiveMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        meet.setVolunteer(null);
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, true, false);
        engagement.setId(1L);
        Meet result = sampleLiveMeet();
        result.setStatus(Status.SENT_TOURIST_REQUEST);
        result.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        result.setVolunteer(sampleVolunteer());
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        when(touristMailProcessor.processRequest(any(), any())).thenReturn(Optional.of(result));
        Meet actual = touristActionService.select(token).get();
        Assert.assertNotNull(actual.getVolunteer());
        Assert.assertEquals(meet.getId(), actual.getId());
    }

    @Test
    public void select_Token_ShouldSelect_WithOnlineMeet() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleOnlineMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        meet.setVolunteer(null);
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, true, false);
        engagement.setId(1L);
        Meet result = sampleOnlineMeet();
        result.setStatus(Status.SENT_TOURIST_REQUEST);
        result.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        result.setVolunteer(sampleVolunteer());
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        when(touristMailProcessor.processRequest(any(), any())).thenReturn(Optional.of(result));
        Meet actual = touristActionService.select(token).get();
        Assert.assertNotNull(actual.getVolunteer());
        Assert.assertEquals(meet.getId(), actual.getId());
    }

    @Test
    public void select_TokenAndBadStatus_ShouldNotSelect_WithLiveMeet() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleLiveMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, true, false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<Meet> actual = touristActionService.select(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void select_TokenAndBadStatus_ShouldNotSelect_WithOnlineMeet() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleOnlineMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, true, false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<Meet> actual = touristActionService.select(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void select_TokenAndBadTime_ShouldNotSelect_WithLiveMeet() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleLiveMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - 550L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, true, false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<Meet> actual = touristActionService.select(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void select_TokenAndBadTime_ShouldNotSelect_WithOnlineMeet() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleOnlineMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - 550L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, true, false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<Meet> actual = touristActionService.select(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void select_TokenAndNotEngaged_ShouldNotSelect_WithLiveMeet() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleLiveMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        meet.setVolunteer(null);
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false, false);
        engagement.setId(1L);
        Meet result = sampleLiveMeet();
        result.setStatus(Status.SENT_TOURIST_REQUEST);
        result.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        result.setVolunteer(sampleVolunteer());
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<Meet> actual = touristActionService.select(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void select_TokenAndNotEngaged_ShouldNotSelect_WithOnlineMeet() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleOnlineMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        meet.setVolunteer(null);
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, false, false);
        engagement.setId(1L);
        Meet result = sampleLiveMeet();
        result.setStatus(Status.SENT_TOURIST_REQUEST);
        result.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        result.setVolunteer(sampleVolunteer());
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<Meet> actual = touristActionService.select(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void select_Token_ShouldFailEdit_WithLiveMeet() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleLiveMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, true, false);
        engagement.setId(1L);
        MeetEngagement result = new MeetEngagement(meet, sampleVolunteer(), time, token, true, false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<Meet> actual = touristActionService.select(token);
        Assert.assertFalse(actual.isPresent());
    }

    @Test
    public void select_Token_ShouldFailEdit_WithOnlineMeet() {
        String token = "A";
        Time time = new Time(10, 10, 10);
        Meet meet = sampleOnlineMeet();
        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10L));
        MeetEngagement engagement = new MeetEngagement(meet, sampleVolunteer(), time, token, true, false);
        engagement.setId(1L);
        MeetEngagement result = new MeetEngagement(meet, sampleVolunteer(), time, token, true, false);
        engagement.setId(1L);
        when(meetEngagementService.getByToken(token)).
                thenReturn(Optional.of(engagement));
        Optional<Meet> actual = touristActionService.select(token);
        Assert.assertFalse(actual.isPresent());
    }
}
