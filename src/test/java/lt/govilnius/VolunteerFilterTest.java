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
        Volunteer volunteer1 = sampleVolunteer(Language.ENGLISH, 26);
        Volunteer volunteer3 = sampleVolunteer(Language.ENGLISH, 36);
        Volunteer volunteer2 = sampleVolunteer(Language.RUSSIAN, 36);
        when(volunteerRepository.findAll()).thenReturn(ImmutableList.of(volunteer1, volunteer2, volunteer3));
        Meet meet = sampleMeet();
        meet.setMeetAgeGroups(ImmutableSet.of(new MeetAgeGroup(AgeGroup.YOUTH, meet)));
        List<Volunteer> volunteers = volunteerFilter.filterByMeet(meet);
        Assert.assertEquals(volunteers.size(), 1);
        Volunteer volunteer = volunteers.get(0);
        Assert.assertEquals(volunteer.getAge().intValue(), 26);
    }

    @Test
    public void filterByMeet_VolunteersWithBadAge_ShouldReturnFiltered() {
        when(volunteerRepository.findAll()).thenReturn(ImmutableList.of(
                sampleVolunteer(Language.RUSSIAN, 36)));
        Meet meet = sampleMeet();
        meet.setMeetAgeGroups(ImmutableSet.of(new MeetAgeGroup(AgeGroup.YOUTH, meet)));
        List<Volunteer> volunteers = volunteerFilter.filterByMeet(meet);
        Assert.assertEquals(volunteers.size(), 0);
    }

    @Test
    public void filterByMeet_VolunteersWithBadLanguage_ShouldReturnFiltered() {
        when(volunteerRepository.findAll()).thenReturn(ImmutableList.of(
                sampleVolunteer(Language.ENGLISH, 26)));
        Meet meet = sampleMeet();
        meet.setLanguages(ImmutableSet.of(new MeetLanguage(Language.RUSSIAN, null)));
        meet.setMeetAgeGroups(ImmutableSet.of(new MeetAgeGroup(AgeGroup.YOUTH, meet)));
        List<Volunteer> volunteers = volunteerFilter.filterByMeet(meet);
        Assert.assertEquals(volunteers.size(), 0);
    }
}
