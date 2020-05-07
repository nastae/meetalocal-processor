package lt.govilnius;

import com.google.common.collect.ImmutableList;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.domainService.filter.MeetEngagementFilter;
import lt.govilnius.domainService.filter.VolunteerFilter;
import lt.govilnius.domainService.mail.EmailSender;
import lt.govilnius.domainService.schedule.VolunteerMailProcessor;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.MeetService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Time;
import java.util.Optional;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static lt.govilnius.EmailSenderTest.sampleVolunteer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(profiles = "dev")
public class VolunteerMailProcessorTest {

    @Mock
    private MeetService meetService;

    @Mock
    private MeetEngagementService meetEngagementService;

    @Mock
    private MeetEngagementFilter meetEngagementFilter;

    @Mock
    private EmailSender emailSender;

    @Mock
    private VolunteerFilter volunteerFilter;

    @InjectMocks
    private VolunteerMailProcessor volunteerMailProcessor;

    @Value("${waiting.sent.volunteer.request.milliseconds}")
    private Long sentVolunteerRequestWaiting;

    @Value("${waiting.evaluation.milliseconds}")
    private Long evaluationWaiting;

    private final static Long mailAcceptingStartHours = 0L;

    private final static Long mailAcceptingEndHours = 24L;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(volunteerMailProcessor, "registrationUrl", "test", String.class);
        ReflectionTestUtils.setField(volunteerMailProcessor, "mailAcceptingStartHours", mailAcceptingStartHours, Long.class);
        ReflectionTestUtils.setField(volunteerMailProcessor, "mailAcceptingEndHours", mailAcceptingEndHours, Long.class);
    }

    @Test
    public void processNews_Meet_ShouldChangeStatusAndSend() {
        Meet meet = sampleMeet();
        Volunteer volunteer = sampleVolunteer();
        MeetEngagement engagement = new MeetEngagement(meet, volunteer, new Time(10, 10, 10), "", false, false);
        when(meetService.findByStatus(Status.NEW)).thenReturn(ImmutableList.of(meet));
        when(volunteerFilter.filterByMeet(meet)).thenReturn(ImmutableList.of(volunteer));
        when(meetEngagementService.create(any(), any(), any())).thenReturn(Optional.of(engagement));
        when(meetEngagementService.setFreezed(any(), anyBoolean())).thenReturn(engagement);
        when(meetService.edit(any(), any())).thenReturn(Optional.of(meet));
        doNothing().when(emailSender).send(any(), any());
        Assert.assertEquals(meet.getStatus(), Status.NEW);
        volunteerMailProcessor.processNews();
        Assert.assertEquals(meet.getStatus(), Status.SENT_VOLUNTEER_REQUEST);
        verify(emailSender, times(1)).send(any(), any());
        verify(meetEngagementService, times(1)).setFreezed(any(), anyBoolean());
    }

    @Test
    public void processNews_Meet_ShouldCancel() {
        Meet meet = sampleMeet();
        Volunteer volunteer = sampleVolunteer();
        MeetEngagement engagement = new MeetEngagement(meet, volunteer, new Time(10, 10, 10), "", false, false);
        when(meetService.findByStatus(Status.NEW)).thenReturn(ImmutableList.of(meet));
        when(volunteerFilter.filterByMeet(meet)).thenReturn(ImmutableList.of());
        when(meetService.edit(any(), any())).thenReturn(Optional.of(meet));
        doNothing().when(emailSender).send(any(), any());
        Assert.assertEquals(meet.getStatus(), Status.NEW);
        volunteerMailProcessor.processNews();
        Assert.assertEquals(meet.getStatus(), Status.CANCELED);
        verify(emailSender, times(1)).send(any(), any());
        verify(meetService, times(0)).setFreezed(any(), anyBoolean());
    }

    /*
    @Test
    public void processVolunteerRequests_SentMeetToVolunteer_ShouldSendRequestToTourist() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
                meet.setChangedAt(new Timestamp(System.currentTimeMillis() - sentVolunteerRequestWaiting));
        meet = meetRepository.save(meet);

        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, new Time(12, 12, 12), "", true);
        meetEngagementRepository.save(meetEngagement);

        volunteerMailProcessor.processRequests();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_TOURIST_REQUEST).size(), 1);
    }

    @Test
    public void processVolunteerRequests_SentMeetToVolunteerNotConfirmed_ShouldSendAdditionToTourist() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - sentVolunteerRequestWaiting));
        meet = meetRepository.save(meet);

        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, new Time(12, 12, 12), "", false);
        meetEngagementRepository.save(meetEngagement);

        volunteerMailProcessor.processRequests();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_TOURIST_ADDITION).size(), 1);
    }

    @Test
    public void processVolunteerRequests_SentMeetToVolunteer_ShouldSendAddition() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - sentVolunteerRequestWaiting));
        meetRepository.save(meet);

        volunteerMailProcessor.processRequests();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_TOURIST_ADDITION).size(), 1);
    }

    @Test
    public void processVolunteerRequests_SentMeetToVolunteerAndBadChangedAt_ShouldDoNothing() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10));
        meetRepository.save(meet);

        volunteerMailProcessor.processRequests();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST).size(), 1);
    }

    @Test
    public void processRequestsAfterAdditional_SentMeetToVolunteer_ShouldSendRequestToTourist() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - sentVolunteerRequestWaiting));
        meet = meetRepository.save(meet);

        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, new Time(12, 12, 12), "", true);
        meetEngagementRepository.save(meetEngagement);

        volunteerMailProcessor.processRequestsAfterAddition();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_TOURIST_REQUEST).size(), 1);
    }

    @Test
    public void processRequestsAfterAdditional_SentMeetToVolunteerNoConfirmed_ShouldSendCancellationToTourist() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - sentVolunteerRequestWaiting));
        meet = meetRepository.save(meet);

        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, new Time(12, 12, 12), "", false);
        meetEngagementRepository.save(meetEngagement);

        volunteerMailProcessor.processRequestsAfterAddition();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.CANCELED).size(), 1);
    }

    @Test
    public void processRequestsAfterAdditional_SentMeetToVolunteer_ShouldNotFindVolunteers() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - sentVolunteerRequestWaiting));
        meetRepository.save(meet);

        volunteerMailProcessor.processRequestsAfterAddition();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.CANCELED).size(), 1);
    }

    @Test
    public void processRequestsAfterAdditional_SentMeetToVolunteerAndBadChangedAt_ShouldDoNothing() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10));
        meetRepository.save(meet);

        volunteerMailProcessor.processRequestsAfterAddition();
        Assert.assertEquals(meetRepository.findByStatus(Status.SENT_VOLUNTEER_REQUEST_AFTER_ADDITION).size(), 1);
    }

    @Test
    public void processAgreements_AgreedMeet_ShouldSendEvaluations() {
        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        Meet meet = sampleMeet();
        meet.setStatus(Status.AGREED);
        meet.setVolunteer(volunteer);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - evaluationWaiting));
        meetRepository.save(meet);

        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, new Time(12, 12, 12), "", false);
        meetEngagementRepository.save(meetEngagement);

        volunteerMailProcessor.processAgreements();
        Assert.assertEquals(meetRepository.findByStatus(Status.AGREED).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.FINISHED).size(), 1);
    }

    @Test
    public void processAgreements_NotAgreedMeet_ShouldRegisterError() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.AGREED);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() - evaluationWaiting));
        meetRepository.save(meet);

        volunteerMailProcessor.processAgreements();
        Assert.assertEquals(meetRepository.findByStatus(Status.AGREED).size(), 0);
        Assert.assertEquals(meetRepository.findByStatus(Status.ERROR).size(), 1);
    }

    @Test
    public void processAgreements_AgreedMeetAndBadChangedAt_ShouldDoNothing() {
        Meet meet = sampleMeet();
        meet.setStatus(Status.AGREED);
        meet.setChangedAt(new Timestamp(System.currentTimeMillis() + 10));
        meetRepository.save(meet);

        Volunteer volunteer = sampleVolunteer();
        volunteerRepository.save(volunteer);

        MeetEngagement meetEngagement = new MeetEngagement(meet, volunteer, new Time(12, 12, 12), "", false);
        meetEngagementRepository.save(meetEngagement);

        volunteerMailProcessor.processAgreements();
        Assert.assertEquals(meetRepository.findByStatus(Status.AGREED).size(), 1);
    }

     */
}
