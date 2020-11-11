package lt.govilnius;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lt.govilnius.domain.reservation.*;
import lt.govilnius.domainService.filter.VolunteerFilter;
import lt.govilnius.facadeService.reservation.VolunteerService;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static lt.govilnius.LiveEmailSenderTest.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(profiles = "dev")
public class VolunteerFilterTest {

    @Mock
    private VolunteerService volunteerService;

    @InjectMocks
    private VolunteerFilter volunteerFilter;

    @Test
    public void filterByMeet_VolunteersWithTimes_ShouldReturnFiltered() {
        Volunteer volunteer1 = sampleVolunteer(Language.ENGLISH);
        volunteer1.getLanguages().add(new VolunteerLanguage(Language.ENGLISH, volunteer1));
        Volunteer volunteer3 = sampleVolunteer(Language.ENGLISH);
        volunteer3.getLanguages().add(new VolunteerLanguage(Language.ENGLISH, volunteer3));
        Volunteer volunteer2 = sampleVolunteer(Language.RUSSIAN);
        volunteer2.getLanguages().add(new VolunteerLanguage(Language.RUSSIAN, volunteer2));
        MeetType meetType = MeetType.LIVE;
        when(volunteerService.findByMeetType(meetType)).thenReturn(ImmutableList.of(volunteer1, volunteer2, volunteer3));
        Meet meet = sampleLiveMeet();
        meet.setMeetAgeGroups(ImmutableSet.of(
                new MeetAgeGroup(AgeGroup.YOUTH, meet),
                new MeetAgeGroup(AgeGroup.JUNIOR_ADULTS, meet),
                new MeetAgeGroup(AgeGroup.SENIOR_ADULTS, meet),
                new MeetAgeGroup(AgeGroup.SENIORS, meet)));
        List<Volunteer> volunteers = volunteerFilter.filterByMeet(meet, meetType);
        Assert.assertEquals(volunteers.size(), 2);
    }

    @Test
    public void filterByMeet_Volunteers_ShouldReturnFiltered() {
        Volunteer volunteer1 = sampleVolunteer(Language.ENGLISH);
        volunteer1.getLanguages().add(new VolunteerLanguage(Language.ENGLISH, volunteer1));
        Volunteer volunteer3 = sampleVolunteer(Language.ENGLISH);
        volunteer3.getLanguages().add(new VolunteerLanguage(Language.ENGLISH, volunteer3));
        Volunteer volunteer2 = sampleVolunteer(Language.RUSSIAN);
        volunteer2.getLanguages().add(new VolunteerLanguage(Language.RUSSIAN, volunteer2));
        MeetType meetType = MeetType.LIVE;
        when(volunteerService.findByMeetType(meetType)).thenReturn(ImmutableList.of(volunteer1, volunteer2, volunteer3));
        Meet meet = sampleLiveMeet();
        meet.setMeetAgeGroups(ImmutableSet.of(
                new MeetAgeGroup(AgeGroup.YOUTH, meet),
                new MeetAgeGroup(AgeGroup.JUNIOR_ADULTS, meet),
                new MeetAgeGroup(AgeGroup.SENIOR_ADULTS, meet),
                new MeetAgeGroup(AgeGroup.SENIORS, meet)));
        List<Volunteer> volunteers = volunteerFilter.filterByMeet(meet, meetType);
        Assert.assertEquals(volunteers.size(), 2);
    }

    @Test
    public void filterByMeet_VolunteersWithBadAge_ShouldReturnFiltered() {
        MeetType meetType = MeetType.LIVE;
        when(volunteerService.findByMeetType(meetType)).thenReturn(ImmutableList.of(
                sampleVolunteer(Language.RUSSIAN)));
        Meet meet = sampleLiveMeet();
        meet.setMeetAgeGroups(ImmutableSet.of(new MeetAgeGroup(AgeGroup.YOUTH, meet)));
        List<Volunteer> volunteers = volunteerFilter.filterByMeet(meet, meetType);
        Assert.assertEquals(volunteers.size(), 0);
    }

    @Test
    public void filterByMeet_VolunteersWithBadLanguage_ShouldReturnFiltered() {
        MeetType meetType = MeetType.LIVE;
        when(volunteerService.findByMeetType(meetType)).thenReturn(ImmutableList.of(
                sampleVolunteer(Language.ENGLISH)));
        Meet meet = sampleLiveMeet();
        meet.setLanguages(ImmutableSet.of(new MeetLanguage(Language.RUSSIAN, null)));
        meet.setMeetAgeGroups(ImmutableSet.of(new MeetAgeGroup(AgeGroup.YOUTH, meet)));
        List<Volunteer> volunteers = volunteerFilter.filterByMeet(meet, meetType);
        Assert.assertEquals(volunteers.size(), 0);
    }
}
