package lt.govilnius.repository.reservation;

import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.MeetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetStatusRepository extends JpaRepository<MeetStatus, Long>,
        CrudRepository<MeetStatus, Long> {

    List<MeetStatus> findByMeetId(Long id);
}
