package lt.govilnius;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.facadeService.reservation.MeetAgeGroupService;
import lt.govilnius.facadeService.reservation.MeetLanguageService;
import lt.govilnius.repository.reservation.MeetAgeGroupRepository;
import lt.govilnius.repository.reservation.MeetLanguageRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles(profiles = "dev")
public class MeetLanguageServiceTest {

    @Mock
    private MeetLanguageRepository repository;

    @InjectMocks
    private MeetLanguageService service;

    @Test
    public void create_MeetAgeGroup_ShouldCreateEntity() {
        final Meet meet = sampleMeet();
        final Language language = Language.ENGLISH;
        final MeetLanguage meetLanguage = new MeetLanguage(language, meet);
        when(repository.save(any())).thenReturn(meetLanguage);
        final Optional<MeetLanguage> result = service.create(language, meet);
        Assert.assertTrue(result.isPresent());
    }

    @Test
    public void create_MeetAgeGroup_ShouldHandleException() {
        final Meet meet = sampleMeet();
        final Language language = Language.ENGLISH;
        when(repository.save(any())).thenThrow(new RuntimeException());
        final Optional<MeetLanguage> result = service.create(language, meet);
        Assert.assertFalse(result.isPresent());
    }
}
