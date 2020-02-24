package lt.govilnius.repository.reservation;

import lt.govilnius.domain.reservation.MeetAgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetAgeGroupRepository extends JpaRepository<MeetAgeGroup, Long> {
}
