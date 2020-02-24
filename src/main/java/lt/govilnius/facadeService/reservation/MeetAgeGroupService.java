package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.AgeGroup;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetAgeGroup;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.repository.reservation.MeetAgeGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeetAgeGroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeetAgeGroupService.class);

    @Autowired
    private MeetAgeGroupRepository repository;

    public Optional<MeetAgeGroup> create(Meet meet, AgeGroup ageGroup) {
        try {
            return Optional.of(repository.save(new MeetAgeGroup(ageGroup, meet)));
        } catch (RuntimeException ex) {
            LOGGER.error("Fail to save a meet age group entity! ", ex);
            return Optional.empty();
        }
    }

    public boolean delete(Long id) {
        Optional<MeetAgeGroup> meetAgeGroup = repository.findById(id);
        if (meetAgeGroup.isPresent()) {
            repository.delete(meetAgeGroup.get());
            return true;
        }
        return false;
    }
}
