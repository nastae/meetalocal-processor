package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.domain.reservation.VolunteerLanguage;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private VolunteerLanguageService volunteerLanguageService;

    public Optional<Volunteer> create(Volunteer volunteer) {
        Volunteer entity = new Volunteer();
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entity.setChangedAt(new Timestamp(System.currentTimeMillis()));
        entity.setName(volunteer.getName());
        entity.setSurname(volunteer.getSurname());
        entity.setDateOfBirth(volunteer.getDateOfBirth());
        entity.setPhoneNumber(volunteer.getPhoneNumber());
        entity.setEmail(volunteer.getEmail());
        entity.setLanguages(volunteer.getLanguages());
        entity.setAge(volunteer.getAge());
        entity.setDescription(volunteer.getDescription());
        entity.setActive(volunteer.getActive());
        entity.setMeetEngagements(new HashSet<>());
        entity = volunteerRepository.save(entity);
        for (VolunteerLanguage language : volunteer.getLanguages()) {
            volunteerLanguageService.create(language.getLanguage(), entity);
        }
        return volunteerRepository.findById(entity.getId());
    }

    public List<Volunteer> getAll() {
        return volunteerRepository.findAll();
    }

    public Optional<Volunteer> get(Long id) {
        return volunteerRepository.findById(id);
    }

    public boolean delete(Long id) {
        Optional<Volunteer> volunteer = volunteerRepository.findById(id);
        if (volunteer.isPresent()) {
            volunteerRepository.delete(volunteer.get());
            return true;
        }
        return false;
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
        entity.setAge(newData.getAge());
        entity.setDescription(newData.getDescription());
        entity.setActive(newData.getActive());
        return volunteerRepository.save(entity);
    }

    public List<Volunteer> findActive() {
        return volunteerRepository.findByActive(true);
    }
}
