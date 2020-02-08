package lt.govilnius.facadeService.reservation;

import io.atlassian.fugue.Either;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.repository.reservation.MeetEngagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

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
        entity.setMeet(meet);
        entity.setVolunteer(volunteer);
        entity.setTime(time);
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
}
