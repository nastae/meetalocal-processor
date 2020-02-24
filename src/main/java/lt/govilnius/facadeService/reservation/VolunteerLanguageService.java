package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.repository.reservation.VolunteerLanguageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VolunteerLanguageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VolunteerLanguageService.class);

    @Autowired
    private VolunteerLanguageRepository repository;

    public Optional<VolunteerLanguage> create(Language language, Volunteer volunteer) {
        try {
            return Optional.of(repository.save(new VolunteerLanguage(language, volunteer)));
        } catch (RuntimeException ex) {
            LOGGER.error("Fail to save a volunteer language entity! ", ex);
            return Optional.empty();
        }
    }
}