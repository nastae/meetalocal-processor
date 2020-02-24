package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.repository.reservation.EvaluationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EvaluationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationService.class);

    @Autowired
    private EvaluationRepository repository;

    public Optional<Evaluation> create(MeetEngagement engagement, String comment, UserType userType) {
        try {
            return Optional.of(repository.save(new Evaluation(engagement, comment, userType)));
        } catch (RuntimeException ex) {
            LOGGER.error("Fail to save a meet age group entity! ", ex);
            return Optional.empty();
        }
    }
}