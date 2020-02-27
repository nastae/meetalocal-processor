package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.Language;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.domain.reservation.VolunteerDto;
import lt.govilnius.domain.reservation.VolunteerLanguage;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private VolunteerLanguageService volunteerLanguageService;

    public Optional<Volunteer> create(VolunteerDto volunteer) {
        Volunteer entity = new Volunteer();
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entity.setChangedAt(new Timestamp(System.currentTimeMillis()));
        entity.setName(volunteer.getName());
        entity.setSurname(volunteer.getSurname());
        entity.setDateOfBirth(volunteer.getDateOfBirth());
        entity.setPhoneNumber(volunteer.getPhoneNumber());
        entity.setEmail(volunteer.getEmail());
        entity.setLanguages(new HashSet<>());
        entity.setDescription(volunteer.getDescription());
        entity.setActive(volunteer.getActive());
        entity.setMeetEngagements(new HashSet<>());
        entity = volunteerRepository.save(entity);
        Set<VolunteerLanguage> languages = new HashSet<>();
        for (String language : volunteer.getLanguages()) {
            final Optional<Language> languageOptional = Language.fromName(language);
            if (languageOptional.isPresent()) {
                Optional<VolunteerLanguage> volunteerLanguage  = volunteerLanguageService.create(languageOptional.get(), entity);
                volunteerLanguage.ifPresent(languages::add);
            }
        }
        entity.setLanguages(languages);
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

    public Optional<Volunteer> edit(Long id, VolunteerDto volunteerDto) {
        final Optional<Volunteer> entity = volunteerRepository.findById(id);
        return entity.map(e -> updateEntity(e, volunteerDto));
    }

    private Volunteer updateEntity(Volunteer entity, VolunteerDto newData) {
        entity.setChangedAt(new Timestamp(System.currentTimeMillis()));
        entity.setName(newData.getName());
        entity.setSurname(newData.getSurname());
        entity.setDateOfBirth(newData.getDateOfBirth());
        entity.setPhoneNumber(newData.getPhoneNumber());
        entity.setEmail(newData.getEmail());
        for (VolunteerLanguage language : entity.getLanguages()) {
            volunteerLanguageService.delete(language.getId());
        }
        Set<VolunteerLanguage> languages = new HashSet<>();
        for (String language : newData.getLanguages()) {
            final Volunteer finalEntity = entity;
            Language.fromName(language).flatMap(l -> volunteerLanguageService.create(l, finalEntity)).ifPresent(languages::add);
        }
        entity.setLanguages(languages);
        entity.setDescription(newData.getDescription());
        entity.setActive(newData.getActive());
        return volunteerRepository.save(entity);
    }

    public List<Volunteer> findActive() {
        return volunteerRepository.findByActive(true);
    }
}
