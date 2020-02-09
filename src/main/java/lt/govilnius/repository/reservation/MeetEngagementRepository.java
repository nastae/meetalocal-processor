package lt.govilnius.repository.reservation;

import lt.govilnius.domain.reservation.MeetEngagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MeetEngagementRepository extends JpaRepository<MeetEngagement, Long>,
        CrudRepository<MeetEngagement, Long> {

    List<MeetEngagement> findByMeetId(Long id);
    List<MeetEngagement> findByVolunteerId(Long id);
    Optional<MeetEngagement> findByToken(String token);
}
