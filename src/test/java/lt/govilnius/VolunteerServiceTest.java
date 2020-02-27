package lt.govilnius;

import com.google.common.collect.ImmutableList;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.domain.reservation.VolunteerDto;
import lt.govilnius.facadeService.reservation.VolunteerLanguageService;
import lt.govilnius.facadeService.reservation.VolunteerActionService;
import lt.govilnius.facadeService.reservation.VolunteerService;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static lt.govilnius.EmailSenderTest.sampleVolunteer;
import static lt.govilnius.MeetServiceTest.sampleVolunteerDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VolunteerServiceTest {

    @Mock
    private VolunteerRepository volunteerRepository;

    @Mock
    private VolunteerLanguageService volunteerLanguageService;

    @InjectMocks
    private VolunteerService volunteerService;

    @Test
    public void create_Volunteer_ShouldBeCreated() {
        VolunteerDto volunteerDto = sampleVolunteerDto();
        Volunteer volunteer = sampleVolunteer();
        when(volunteerRepository.save(any())).thenReturn(volunteer);
        when(volunteerLanguageService.create(any(), any())).thenReturn(Optional.empty());
        when(volunteerRepository.findById(any())).thenReturn(Optional.of(volunteer));
        Optional<Volunteer> result = volunteerService.create(volunteerDto);
        Assert.assertTrue(result.isPresent());
    }

    @Test
    public void getAll_Volunteers_ShouldGet() {
        when(volunteerRepository.findAll()).thenReturn(ImmutableList.of(new Volunteer()));
        final List<Volunteer> meets = volunteerService.getAll();
        Assert.assertEquals(meets.size(), 1L);
    }

    @Test
    public void get_Volunteer_ShouldGetById() {
        final Volunteer volunteer = sampleVolunteer();
        when(volunteerRepository.findById(1L)).thenReturn(Optional.of(volunteer));
        final Optional<Volunteer> result = volunteerService.get(1L);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getId(), volunteer.getId());
    }

    @Test
    public void get_Volunteer_ShouldNotGetById() {
        final Volunteer volunteer = sampleVolunteer();
        when(volunteerRepository.findById(1L)).thenReturn(Optional.empty());
        final Optional<Volunteer> result = volunteerService.get(1L);
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void delete_Volunteer_ShouldBeDeleted() {
        Volunteer volunteer = sampleVolunteer();
        volunteer.setId(1L);
        when(volunteerRepository.findById(any())).thenReturn(Optional.of(volunteer));
        doNothing().when(volunteerRepository).delete(any());
        boolean result = volunteerService.delete(volunteer.getId());
        Assert.assertTrue(result);
    }

    @Test
    public void edit_Volunteer_ShouldEdit() {
        Volunteer oldVolunteer = sampleVolunteer();
        oldVolunteer.setId(1L);
        VolunteerDto newVolunteer = sampleVolunteerDto();
        newVolunteer.setId(2L);
        when(volunteerRepository.findById(oldVolunteer.getId())).thenReturn(Optional.of(oldVolunteer));
        when(volunteerRepository.save(oldVolunteer)).thenReturn(oldVolunteer);
        final Optional<Volunteer> result = volunteerService.edit(oldVolunteer.getId(), newVolunteer);
        Assert.assertTrue(result.isPresent());
        Assert.assertEquals(result.get().getId(), oldVolunteer.getId());
    }

    @Test
    public void edit_NotExistVolunteer_ShouldNotEdit() {
        final VolunteerDto volunteerDto = sampleVolunteerDto();
        final Volunteer volunteer = sampleVolunteer();
        volunteer.setId(1L);
        when(volunteerRepository.findById(volunteer.getId())).thenReturn(Optional.of(volunteer));
        when(volunteerRepository.save(any())).thenReturn(volunteer);
        final Optional<Volunteer> result = volunteerService.edit(1L, volunteerDto);
        Assert.assertTrue(result.isPresent());
    }

    @Test
    public void findActive_Volunteers_ShouldGet() {
        when(volunteerRepository.findByActive(true)).thenReturn(ImmutableList.of(new Volunteer()));
        final List<Volunteer> volunteers = volunteerService.findActive();
        Assert.assertEquals(volunteers.size(), 1L);
    }
}
