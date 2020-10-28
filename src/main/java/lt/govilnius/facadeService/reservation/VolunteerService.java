package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    public Optional<Volunteer> create(VolunteerDto volunteer) {
        Volunteer entity = new Volunteer();
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entity.setChangedAt(new Timestamp(System.currentTimeMillis()));
        entity.setName(volunteer.getName());
        entity.setSurname(volunteer.getSurname());
        Calendar date = Calendar.getInstance();
        date.setTime(volunteer.getDateOfBirth());
        entity.setDateOfBirth(new Date(date.getTimeInMillis()));
        entity.setPhoneNumber(volunteer.getPhoneNumber());
        entity.setEmail(volunteer.getEmail());
        entity.setLanguages(new HashSet<>());
        entity.setDescription(volunteer.getDescription());
        entity.setActive(volunteer.getActive());
        entity.setMeetEngagements(new HashSet<>());
        for (String language : volunteer.getLanguages()) {
            final Optional<Language> languageOptional = Language.fromName(language);
            if (languageOptional.isPresent()) {
                entity.getLanguages().add(new VolunteerLanguage(languageOptional.get(), entity));
            }
        }
        for (String purpose : volunteer.getPurposes()) {
            Optional<Purpose> purposeOptional = Purpose.fromName(purpose);
            if (purposeOptional.isPresent()) {
                entity.getPurposes().add(new VolunteerPurpose(purposeOptional.get(), entity));
            }
        }
        entity = volunteerRepository.save(entity);
        return volunteerRepository.findById(entity.getId());
    }

    public List<Volunteer> getAll() {
        return volunteerRepository.findAll();
    }

    public List<Volunteer> getSortedByIdAll() {
        List<Volunteer> volunteers = this.getAll();
        volunteers.sort((e1, e2) -> (int) (e2.getId() - e1.getId()));
        return volunteers;
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
        entity.getPurposes().clear();
        for (String purpose : newData.getPurposes()) {
            final Volunteer finalEntity = entity;
            Purpose.fromName(purpose).map(l -> new VolunteerPurpose(l, finalEntity)).ifPresent(p -> entity.getPurposes().add(p));
        }
        Calendar date = Calendar.getInstance();
        date.setTime(newData.getDateOfBirth());
        entity.setDateOfBirth(new Date(date.getTimeInMillis()));
        entity.setPhoneNumber(newData.getPhoneNumber());
        entity.setEmail(newData.getEmail());
        entity.getLanguages().clear();
        for (String language : newData.getLanguages()) {
            final Volunteer finalEntity = entity;
            Language.fromName(language).map(l -> new VolunteerLanguage(l, finalEntity)).ifPresent(p -> entity.getLanguages().add(p));
        }
        entity.setDescription(newData.getDescription());
        entity.setActive(newData.getActive());
        return volunteerRepository.save(entity);
    }

    public List<Volunteer> findActive() {
        return volunteerRepository.findByActive(true);
    }
}
