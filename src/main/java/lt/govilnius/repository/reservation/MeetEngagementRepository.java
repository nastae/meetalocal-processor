package lt.govilnius.repository.reservation;

import lt.govilnius.domain.reservation.MeetEngagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetEngagementRepository extends JpaRepository<MeetEngagement, Long> {
}
