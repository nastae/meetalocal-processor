package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.repository.reservation.MeetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.*;

@Service
public class MeetService {

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private MeetStatusService meetStatusService;

    @Autowired
    private MeetLanguageService meetLanguageService;

    @Autowired
    private MeetAgeGroupService meetAgeGroupService;

    public Optional<Meet> create(MeetDto meet) {
        Meet entity = new Meet();
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entity.setChangedAt(new Timestamp(System.currentTimeMillis()));
        entity.setName(meet.getName());
        entity.setSurname(meet.getSurname());
        entity.setEmail(meet.getEmail());
        entity.setPhoneNumber(meet.getPhoneNumber());
        entity.setCountry(meet.getCountry());
        Calendar date = Calendar.getInstance();
        date.setTime(meet.getDate());
        entity.setDate(new Date(date.getTimeInMillis()));
        Calendar time = Calendar.getInstance();
        LocalTime localTime = meet.getTime();
        time.set(0, 0, 0, localTime.getHour(), localTime.getMinute(), localTime.getSecond());
        entity.setTime(new Time(time.getTimeInMillis()));
        entity.setPeopleCount(meet.getPeopleCount());
        entity.setAge(meet.getAge());
        entity.setMeetAgeGroups(new HashSet<>());
        entity.setLanguages(new HashSet<>());
        entity.setPreferences(meet.getPreferences());
        entity.setAdditionalPreferences(meet.getAdditionalPreferences());
        entity.setVolunteer(null);
        entity.setFreezed(false);
        entity.setStatus(Status.NEW);
        entity = meetRepository.save(entity);
        for (AgeGroup ageGroup : meet.getAgeGroups()) {
            meetAgeGroupService.create(entity, ageGroup);
        }
        for (Language language : meet.getLanguages()) {
            meetLanguageService.create(language, entity);
        }
        meetStatusService.create(entity, entity.getStatus());
        return meetRepository.findById(entity.getId());
    }

    public Meet setVolunteer(Meet meet, Volunteer volunteer) {
        meet.setVolunteer(volunteer);
        return meetRepository.save(meet);
    }

    public Meet setFreezed(Meet meet, boolean freezed) {
        meet.setFreezed(freezed);
        return meetRepository.save(meet);
    }

    public List<Meet> getAll() {
        return meetRepository.findAll();
    }

    public Optional<Meet> get(Long id) {
        return meetRepository.findById(id);
    }

    public List<Meet> getSortedByIdAll() {
        List<Meet> meets = this.getAll();
        meets.sort((e1, e2) -> (int) (e2.getId() - e1.getId()));
        return meets;
    }

    public Optional<Meet> edit(Long id, Meet meet) {
        final Optional<Meet> entity = meetRepository.findById(id);
        return entity.map(e -> updateEntity(e, meet));
    }

    private Meet updateEntity(Meet entity, Meet newData) {
        if (!newData.getStatus().equals(entity.getStatus())) {
            meetStatusService.create(entity, entity.getStatus());
        }
        entity.setStatus(newData.getStatus());
        entity.setChangedAt(new Timestamp(System.currentTimeMillis()));
        entity = meetRepository.save(entity);
        return entity;
    }

    public List<Meet> findByStatus(Status status) {
        return meetRepository.findByStatus(status);
    }
}
