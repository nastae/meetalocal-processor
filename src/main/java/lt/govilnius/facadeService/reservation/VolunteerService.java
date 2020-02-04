package lt.govilnius.facadeService.reservation;

import io.atlassian.fugue.Either;
import lt.govilnius.domain.reservation.*;
import lt.govilnius.repository.reservation.VolunteerLanguageRepository;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private VolunteerLanguageRepository volunteerLanguageRepository;

    public Either<Exception, Volunteer> create(Volunteer volunteer) {
        try {
            return Either.right(add(volunteer));
        } catch (RuntimeException e) {
            return Either.left(new RuntimeException("Fail to create a volunteer entity"));
        }
    }

    public Either<Exception, VolunteerLanguage> addLanguage(Long id, Language language) {
        final Optional<Volunteer> optionalVolunteer = volunteerRepository.findById(id);
        return optionalVolunteer
                .<Either<Exception, VolunteerLanguage>>map(volunteer -> Either.right(volunteerLanguageRepository.save(new VolunteerLanguage(language, volunteer))))
                .orElseGet(() -> Either.left(new NoSuchElementException("Volunteer doesn't exist with id " + id)));
    }

    private Volunteer add(Volunteer volunteer) {
        final Volunteer entity = new Volunteer();
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entity.setChangedAt(new Timestamp(System.currentTimeMillis()));
        entity.setName(volunteer.getName());
        entity.setSurname(volunteer.getSurname());
        entity.setDateOfBirth(volunteer.getDateOfBirth());
        entity.setPhoneNumber(volunteer.getPhoneNumber());
        entity.setEmail(volunteer.getEmail());
        entity.setLanguages(volunteer.getLanguages());
        entity.setAdditionalLanguages(volunteer.getAdditionalLanguages());
        entity.setAge(volunteer.getAge());
        entity.setGender(volunteer.getGender());
        entity.setDescription(volunteer.getDescription());
        entity.setActive(volunteer.getActive());
        return volunteerRepository.save(entity);
    }

    public List<Volunteer> getAll() {
        return volunteerRepository.findAll();
    }

    public Optional<Volunteer> get(Long id) {
        return volunteerRepository.findById(id);
    }

    public Optional<Volunteer> delete(Long id) {
        return volunteerRepository.findById(id)
                .map(this::deleteEntity);
    }

    private Volunteer deleteEntity(Volunteer entity) {
        volunteerRepository.delete(entity);
        return entity;
    }

    public Optional<Volunteer> edit(Long id, Volunteer volunteer) {
        final Optional<Volunteer> entity = volunteerRepository.findById(id);
        return entity.map(e -> updateEntity(e, volunteer));
    }

    private Volunteer updateEntity(Volunteer entity, Volunteer newData) {
        entity.setChangedAt(new Timestamp(System.currentTimeMillis()));
        entity.setName(newData.getName());
        entity.setSurname(newData.getSurname());
        entity.setDateOfBirth(newData.getDateOfBirth());
        entity.setPhoneNumber(newData.getPhoneNumber());
        entity.setEmail(newData.getEmail());
        entity.setLanguages(newData.getLanguages());
        entity.setAdditionalLanguages(newData.getAdditionalLanguages());
        entity.setAge(newData.getAge());
        entity.setGender(newData.getGender());
        entity.setDescription(newData.getDescription());
        entity.setActive(newData.getActive());
        return volunteerRepository.save(entity);
    }

    public List<Volunteer> findActive() {
        return volunteerRepository.findByActive(true);
    }
}
