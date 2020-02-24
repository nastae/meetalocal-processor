package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.Language;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetLanguage;
import lt.govilnius.repository.reservation.MeetLanguageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeetLanguageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeetLanguageService.class);

    @Autowired
    private MeetLanguageRepository repository;

    public Optional<MeetLanguage> create(Language language, Meet meet) {
        try {
            return Optional.of(repository.save(new MeetLanguage(language, meet)));
        } catch (RuntimeException ex) {
            LOGGER.error("Fail to save a meet language entity! ", ex);
            return Optional.empty();
        }
    }
}
