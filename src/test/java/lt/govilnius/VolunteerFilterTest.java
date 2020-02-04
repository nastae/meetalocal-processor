package lt.govilnius;

import com.google.common.collect.ImmutableList;
import lt.govilnius.domain.reservation.Gender;
import lt.govilnius.domain.reservation.Volunteer;
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
                sampleVolunteer(Gender.MALE, 26),
                sampleVolunteer(Gender.MALE, 30),
                sampleVolunteer(Gender.MALE, 1),
                sampleVolunteer(Gender.FEMALE, 26)));
        List<Volunteer> volunteers = volunteerFilter.filterByMeet(sampleMeet());
        Assert.assertEquals(volunteers.size(), 1);
        Volunteer volunteer = volunteers.get(0);
        Assert.assertEquals(volunteer.getAge().intValue(), 26);
        Assert.assertEquals(volunteer.getGender(), Gender.MALE);
    }
}
