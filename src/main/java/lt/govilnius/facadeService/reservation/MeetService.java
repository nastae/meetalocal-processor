package lt.govilnius.facadeService.reservation;

import io.atlassian.fugue.Either;
import lt.govilnius.domain.reservation.Language;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetLanguage;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.repository.reservation.MeetLanguageRepository;
import lt.govilnius.repository.reservation.MeetRepository;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MeetService {

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private MeetLanguageRepository meetLanguageRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    public Optional<Meet> create(Meet meet) {
        return add(meet);
    }

    public Either<Exception, Meet> setVolunteer(Long id, Long volunteerId) {
        final Optional<Meet> optionalMeet = meetRepository.findById(id);
        if (optionalMeet.isPresent()) {
            final Meet meet = optionalMeet.get();
            if (meet.getVolunteer() != null) {
                return Either.left(new NoSuchFieldException("Volunteer is set in the meet with id " + id));
            }
            meet.setVolunteer(volunteerRepository.findById(volunteerId).orElseThrow(RuntimeException::new));
            return Either.right(meetRepository.save(meet));
        } else {
            return Either.left(new NoSuchElementException("Meet doesn't exist with id " + id));
        }
    }

    private Either<Exception, MeetLanguage> addLanguage(Long id, Language language) {
        final Optional<Meet> optionalMeet = meetRepository.findById(id);
        return optionalMeet
                .<Either<Exception, MeetLanguage>>map(meet -> Either.right(meetLanguageRepository.save(new MeetLanguage(language, meet))))
                .orElseGet(() -> Either.left(new NoSuchElementException("Meet doesn't exist with id " + id)));
    }

    private Optional<Meet> add(Meet meet) {
        final Meet entity = new Meet();
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entity.setChangedAt(new Timestamp(System.currentTimeMillis()));
        entity.setEmail(meet.getEmail());
        entity.setPhoneNumber(meet.getPhoneNumber());
        entity.setName(meet.getName());
        entity.setSurname(meet.getSurname());
        entity.setResidence(meet.getResidence());
        entity.setDate(meet.getDate());
        entity.setTime(meet.getTime());
        entity.setPeopleCount(meet.getPeopleCount());
        entity.setAge(meet.getAge());
        entity.setGender(meet.getGender());
        entity.setAgeGroup(meet.getAgeGroup());
        entity.setLanguages(meet.getLanguages());
        entity.setPreferences(meet.getPreferences());
        entity.setComment(meet.getComment());
        entity.setVolunteer(null);
        entity.setStatus(Status.NEW);
        meet = meetRepository.save(entity);
        for (MeetLanguage language : meet.getLanguages()) {
            addLanguage(meet.getId(), language.getLanguage());
        }
        return get(meet.getId());
    }

    public List<Meet> getAll() {
        return meetRepository.findAll();
    }

    public Optional<Meet> get(Long id) {
        return meetRepository.findById(id);
    }

    public Optional<Meet> delete(Long id) {
        return meetRepository.findById(id)
                .map(this::deleteEntity);
    }

    private Meet deleteEntity(Meet entity) {
        meetRepository.delete(entity);
        return entity;
    }

    public Optional<Meet> edit(Long id, Meet meet) {
        final Optional<Meet> entity = meetRepository.findById(id);
        return entity.map(e -> updateEntity(e, meet));
    }

    private Meet updateEntity(Meet entity, Meet newData) {
        entity.setStatus(newData.getStatus());
        entity.setChangedAt(new Timestamp(System.currentTimeMillis()));
        return meetRepository.save(entity);
    }

    public List<Meet> findByStatus(Status status) {
        return meetRepository.findByStatus(status);
    }
}
