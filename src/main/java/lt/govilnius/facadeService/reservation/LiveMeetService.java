package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.repository.reservation.LiveMeetRepository;
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
public class LiveMeetService {

    @Autowired
    private LiveMeetRepository liveMeetRepository;

    @Autowired
    private MeetStatusService meetStatusService;

    @Autowired
    private MeetLanguageService meetLanguageService;

    @Autowired
    private MeetAgeGroupService meetAgeGroupService;

    public Optional<LiveMeet> create(LiveMeetDto meet) {
        LiveMeet entity = new LiveMeet();
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entity.setChangedAt(new Timestamp(System.currentTimeMillis()));
        entity.setName(meet.getName());
        entity.setSurname(meet.getSurname());
        entity.setPurpose(meet.getPurpose());
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
        entity = liveMeetRepository.save(entity);
        for (AgeGroup ageGroup : meet.getAgeGroups()) {
            meetAgeGroupService.create(entity, ageGroup);
        }
        for (Language language : meet.getLanguages()) {
            meetLanguageService.create(language, entity);
        }
        meetStatusService.create(entity, entity.getStatus());
        return liveMeetRepository.findById(entity.getId());
    }
}
