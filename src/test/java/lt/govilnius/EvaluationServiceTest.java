package lt.govilnius;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.facadeService.reservation.EvaluationService;
import lt.govilnius.repository.reservation.EvaluationRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EvaluationServiceTest {

    @Mock
    private EvaluationRepository repository;

    @InjectMocks
    private EvaluationService service;

    @Test
    public void create_MeetAgeGroup_ShouldCreateEntity() {
        final MeetEngagement engagement = new MeetEngagement();
        final Evaluation evaluation = new Evaluation(engagement, "comment", UserType.TOURIST);
        when(repository.save(any())).thenReturn(evaluation);
        final Optional<Evaluation> result = service.create(engagement, "comment", UserType.TOURIST);
        Assert.assertTrue(result.isPresent());
    }

    @Test
    public void create_MeetAgeGroup_ShouldHandleException() {
        final MeetEngagement engagement = new MeetEngagement();
        when(repository.save(any())).thenThrow(new RuntimeException());
        final Optional<Evaluation> result = service.create(engagement, "comment", UserType.TOURIST);
        Assert.assertFalse(result.isPresent());
    }
}
