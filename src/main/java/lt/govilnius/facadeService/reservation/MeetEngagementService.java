package lt.govilnius.facadeService.reservation;

import io.atlassian.fugue.Either;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.domainService.security.Encryptor;
import lt.govilnius.repository.reservation.MeetEngagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
public class MeetEngagementService {

    @Autowired
    private MeetEngagementRepository meetEngagementRepository;

    public Either<Exception, MeetEngagement> create(Meet meet, Volunteer volunteer, Time time) {
        try {
            return Either.right(add(meet, volunteer, time));
        } catch (RuntimeException e) {
            return Either.left(new RuntimeException("Fail to create a meet engagement entity"));
        }
    }

    private MeetEngagement add(Meet meet, Volunteer volunteer, Time time) {
        final MeetEngagement entity = new MeetEngagement();
        final String token = Encryptor.encrypt(meet.getId().toString() + volunteer.getId().toString());
        final Optional<MeetEngagement> meetEngagement = meetEngagementRepository.findByToken(token);
        meetEngagement.ifPresent(meetEngagement1 -> entity.setId(meetEngagement1.getId()));
        entity.setMeet(meet);
        entity.setVolunteer(volunteer);
        entity.setTime(time);
        entity.setToken(token);
        entity.setEngaged(false);
        return meetEngagementRepository.save(entity);
    }

    public Optional<MeetEngagement> edit(Long id, MeetEngagement meetEngagement) {
        final Optional<MeetEngagement> entity = meetEngagementRepository.findById(id);
        return entity.map(e -> updateEntity(e, meetEngagement));
    }

    private MeetEngagement updateEntity(MeetEngagement entity, MeetEngagement newData) {
        entity.setEngaged(newData.getEngaged());
        return meetEngagementRepository.save(entity);
    }

    public List<MeetEngagement> getAll() {
        return meetEngagementRepository.findAll();
    }

    public List<MeetEngagement> getByMeetId(Long meetId) {
        return meetEngagementRepository.findByMeetId(meetId);
    }

    public List<MeetEngagement> getByVolunteerId(Long volunteerId) {
        return meetEngagementRepository.findByVolunteerId(volunteerId);
    }

    public Optional<MeetEngagement> getByToken(String token) {
        return meetEngagementRepository.findByToken(token);
    }
}
