package lt.govilnius;

import lt.govilnius.domain.reservation.AgeGroup;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetAgeGroup;
import lt.govilnius.facadeService.reservation.MeetAgeGroupService;
import lt.govilnius.repository.reservation.MeetAgeGroupRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MeetAgeGroupServiceTest {

    @Mock
    private MeetAgeGroupRepository repository;

    @InjectMocks
    private MeetAgeGroupService service;

    @Test
    public void create_MeetAgeGroup_ShouldCreateEntity() {
        final Meet meet = sampleMeet();
        final AgeGroup ageGroup = AgeGroup.JUNIOR_ADULTS;
        final MeetAgeGroup meetAgeGroup = new MeetAgeGroup(ageGroup, meet);
        when(repository.save(any())).thenReturn(meetAgeGroup);
        final Optional<MeetAgeGroup> result = service.create(meet, ageGroup);
        Assert.assertTrue(result.isPresent());
    }

    @Test
    public void create_MeetAgeGroup_ShouldHandleException() {
        final Meet meet = sampleMeet();
        final AgeGroup ageGroup = AgeGroup.JUNIOR_ADULTS;
        when(repository.save(any())).thenThrow(new RuntimeException());
        final Optional<MeetAgeGroup> result = service.create(meet, ageGroup);
        Assert.assertFalse(result.isPresent());
    }

    @Test
    public void delete_MeetAgeGroup_ShouldBeDeleted() {
        MeetAgeGroup meetAgeGroup = new MeetAgeGroup(AgeGroup.YOUTH, sampleMeet());
        meetAgeGroup.setId(1L);
        when(repository.findById(meetAgeGroup.getId())).thenReturn(Optional.of(meetAgeGroup));
        doNothing().when(repository).delete(meetAgeGroup);
        boolean result = service.delete(meetAgeGroup.getId());
        Assert.assertTrue(result);
    }
}
