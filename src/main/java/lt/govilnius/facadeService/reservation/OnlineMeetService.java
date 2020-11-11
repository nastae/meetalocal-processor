package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.repository.reservation.MeetRepository;
import lt.govilnius.repository.reservation.OnlineMeetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Optional;

@Service
public class OnlineMeetService {

    @Autowired
    private OnlineMeetRepository onlineMeetRepository;

    @Autowired
    private MeetStatusService meetStatusService;

    @Autowired
    private MeetLanguageService meetLanguageService;

    @Autowired
    private MeetAgeGroupService meetAgeGroupService;

    public Optional<OnlineMeet> create(OnlineMeetDto meet) {
        OnlineMeet entity = new OnlineMeet();
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entity.setChangedAt(new Timestamp(System.currentTimeMillis()));
        entity.setName(meet.getName());
        entity.setSurname(meet.getSurname());
        entity.setPurpose(meet.getPurpose());
        entity.setEmail(meet.getEmail());
        entity.setSkypeName(meet.getSkypeName());
        entity.setCountry(meet.getCountry());
        Calendar date = Calendar.getInstance();
        date.setTime(meet.getDate());
        entity.setDate(new Date(date.getTimeInMillis()));
        Calendar time = Calendar.getInstance();
        LocalTime localTime = meet.getTime();
        time.set(0, 0, 0, localTime.getHour(), localTime.getMinute(), localTime.getSecond());
        entity.setTime(new Time(time.getTimeInMillis()));
        entity.setAge(meet.getAge());
        entity.setMeetAgeGroups(new HashSet<>());
        entity.setLanguages(new HashSet<>());
        entity.setPreferences(meet.getPreferences());
        entity.setAdditionalPreferences(meet.getAdditionalPreferences());
        entity.setVolunteer(null);
        entity.setFreezed(false);
        entity.setStatus(Status.NEW);
        entity = onlineMeetRepository.save(entity);
        for (AgeGroup ageGroup : meet.getAgeGroups()) {
            meetAgeGroupService.create(entity, ageGroup);
        }
        for (Language language : meet.getLanguages()) {
            meetLanguageService.create(language, entity);
        }
        meetStatusService.create(entity, entity.getStatus());
        return onlineMeetRepository.findById(entity.getId());
    }
}
