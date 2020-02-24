package lt.govilnius;

import lt.govilnius.domain.reservation.Language;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.domain.reservation.VolunteerLanguage;
import lt.govilnius.facadeService.reservation.VolunteerLanguageService;
import lt.govilnius.repository.reservation.VolunteerLanguageRepository;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static lt.govilnius.EmailSenderTest.sampleVolunteer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VolunteerLanguageServiceTest {

    @Mock
    private VolunteerLanguageRepository repository;

    @InjectMocks
    private VolunteerLanguageService service;

    @Test
    public void create_VolunteerLanguage_ShouldCreateEntity() {
        Volunteer volunteer = sampleVolunteer();
        Language language = Language.ENGLISH;
        VolunteerLanguage volunteerLanguage = new VolunteerLanguage(language, volunteer);
        when(repository.save(any())).thenReturn(volunteerLanguage);
        final Optional<VolunteerLanguage> result = service.create(language, volunteer);
        Assert.assertTrue(result.isPresent());
    }

    @Test
    public void create_VolunteerLanguage_ShouldHandleException() {
        final Volunteer volunteer = sampleVolunteer();
        final Language language = Language.ENGLISH;
        when(repository.save(any())).thenThrow(new RuntimeException());
        final Optional<VolunteerLanguage> result = service.create(language, volunteer);
        Assert.assertFalse(result.isPresent());
    }
}
