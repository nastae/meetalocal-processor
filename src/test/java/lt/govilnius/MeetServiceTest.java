package lt.govilnius;

import com.google.common.collect.ImmutableList;
import lt.govilnius.domain.reservation.*;
import lt.govilnius.facadeService.reservation.MeetAgeGroupService;
import lt.govilnius.facadeService.reservation.MeetLanguageService;
import lt.govilnius.facadeService.reservation.MeetService;
import lt.govilnius.facadeService.reservation.MeetStatusService;
import lt.govilnius.repository.reservation.MeetRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static lt.govilnius.LiveEmailSenderTest.sampleLiveMeet;
import static lt.govilnius.LiveEmailSenderTest.sampleVolunteer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(profiles = "dev")
public class MeetServiceTest {

    @Mock
    private MeetRepository meetRepository;

    @Mock
    private MeetStatusService meetStatusService;

    @Mock
    private MeetLanguageService meetLanguageService;

    @Mock
    private MeetAgeGroupService meetAgeGroupService;

    @InjectMocks
    private MeetService meetService;

    @Test
    public void getAll_Meets_ShouldGet() {
        when(meetRepository.findAll()).thenReturn(ImmutableList.of(new Meet()));
        final List<Meet> meets = meetService.getAll();
        Assert.assertEquals(meets.size(), 1L);
    }

    @Test
    public void setVolunteer_Meet_ShouldSetVolunteer() {
        Meet meet = sampleLiveMeet();
        meet.setVolunteer(null);
        Volunteer volunteer = sampleVolunteer();
        when(meetRepository.save(meet)).thenReturn(meet);
        final Meet result = meetService.setVolunteer(meet, volunteer);
        Assert.assertNotEquals(result.getVolunteer(), null);
    }

    @Test
    public void setFreezed_Meet_ShouldSetVolunteer() {
        Meet meet = sampleLiveMeet();
        meet.setFreezed(false);
        when(meetRepository.save(meet)).thenReturn(meet);
        final Meet result = meetService.setFreezed(meet, true);
        Assert.assertEquals(result.getFreezed(), true);
    }

    @Test
    public void get_Meet_ShouldGetById() {
        final Meet meet = sampleLiveMeet();
        when(meetRepository.findById(1L)).thenReturn(Optional.of(meet));
        final Optional<Meet> result = meetService.get(1L);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getId(), meet.getId());
    }

    @Test
    public void get_Meet_ShouldNotGetById() {
        final Meet meet = sampleLiveMeet();
        when(meetRepository.findById(1L)).thenReturn(Optional.empty());
        final Optional<Meet> result = meetService.get(1L);
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void edit_Meet_ShouldEdit() {
        Meet oldMeet = sampleLiveMeet();
        oldMeet.setId(1L);
        oldMeet.setStatus(Status.NEW);
        Meet newMeet = sampleLiveMeet();
        newMeet.setId(2L);
        newMeet.setStatus(Status.CANCELED);
        when(meetStatusService.create(any(), any())).thenReturn(Optional.empty());
        when(meetRepository.findById(oldMeet.getId())).thenReturn(Optional.of(oldMeet));
        when(meetRepository.save(oldMeet)).thenReturn(oldMeet);
        final Optional<Meet> result = meetService.edit(oldMeet.getId(), newMeet);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getStatus(), newMeet.getStatus());
    }

    @Test
    public void edit_NotExistMeet_ShouldNotEdit() {
        final Meet meet = sampleLiveMeet();
        when(meetRepository.findById(1L)).thenReturn(Optional.empty());
        final Optional<Meet> result = meetService.edit(1L, meet);
        Assert.assertFalse(result.isPresent());
    }

    public static LiveMeetDto sampleLiveMeetDto() {
        return new LiveMeetDto("name", "surname", Purpose.RELOCATION, "meetalocaltest@gmail.com", "123",
                "Lithuania", new Date(2019, 1, 1),
                LocalTime.of(11, 11, 11),
                1, "18-29", ImmutableList.of(AgeGroup.YOUTH),
                ImmutableList.<Language>builder().add(Language.ENGLISH).build(), "preferences", "additionalPreferences");
    }

    public static OnlineMeetDto sampleOnlineMeetDto() {
        return new OnlineMeetDto("name", "surname", Purpose.RELOCATION, "meetalocaltest@gmail.com",
                "Lithuania", new Date(2019, 1, 1),
                LocalTime.of(11, 11, 11),
                "18-29", ImmutableList.of(AgeGroup.YOUTH),
                ImmutableList.<Language>builder().add(Language.ENGLISH).build(), "preferences", "more", "skype");
    }

    public static VolunteerDto sampleVolunteerDto() {
        List<String> purposes = new ArrayList<>();
        List<String> meetTypes = new ArrayList<>();
        meetTypes.add(MeetType.LIVE.getName());
        meetTypes.add(MeetType.ONLINE.getName());
        purposes.add("Relocation");
        return new VolunteerDto(1L,
                "name", "surname", new Date(1999, 11, 11), "A",
                "123", "meetalocaltest@gmail.com", ImmutableList.of(Language.ENGLISH.name()),
                "description", true, purposes, meetTypes);
    }
}
