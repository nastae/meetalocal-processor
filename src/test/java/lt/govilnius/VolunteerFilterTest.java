package lt.govilnius;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lt.govilnius.domain.reservation.*;
import lt.govilnius.domainService.filter.VolunteerFilter;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static lt.govilnius.EmailSenderTest.sampleVolunteer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VolunteerFilterTest {

    @Mock
    private VolunteerRepository volunteerRepository;

    @InjectMocks
    private VolunteerFilter volunteerFilter;

    @Test
    public void filterByMeet_Volunteers_ShouldReturnFiltered() {
        when(volunteerRepository.findAll()).thenReturn(ImmutableList.of(
                sampleVolunteer(Language.RUSSIAN, 26),
                sampleVolunteer(Language.RUSSIAN, 30),
                sampleVolunteer(Language.RUSSIAN, 1),
                sampleVolunteer(Language.ENGLISH, 26)));
        List<Volunteer> volunteers = volunteerFilter.filterByMeet(sampleMeet());
        Assert.assertEquals(volunteers.size(), 1);
        Volunteer volunteer = volunteers.get(0);
        Assert.assertEquals(volunteer.getAge().intValue(), 26);
        Assert.assertEquals(volunteer.getGender(), Gender.MALE);
    }

    @Test
    public void filterByMeet_VolunteersWithBadAge_ShouldReturnFiltered() {
        when(volunteerRepository.findAll()).thenReturn(ImmutableList.of(
                sampleVolunteer(Language.RUSSIAN, 36)));
        Meet meet = sampleMeet();
        meet.setAgeGroup(AgeGroup.YOUTH);
        List<Volunteer> volunteers = volunteerFilter.filterByMeet(meet);
        Assert.assertEquals(volunteers.size(), 0);
    }

    @Test
    public void filterByMeet_VolunteersWithBadLanguage_ShouldReturnFiltered() {
        when(volunteerRepository.findAll()).thenReturn(ImmutableList.of(
                sampleVolunteer(Language.ENGLISH, 26)));
        Meet meet = sampleMeet();
        meet.setLanguages(ImmutableSet.of(new MeetLanguage(Language.RUSSIAN, null)));
        meet.setAgeGroup(AgeGroup.YOUTH);
        List<Volunteer> volunteers = volunteerFilter.filterByMeet(meet);
        Assert.assertEquals(volunteers.size(), 0);
    }
}
