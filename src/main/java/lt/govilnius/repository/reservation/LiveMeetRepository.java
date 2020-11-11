package lt.govilnius.repository.reservation;

import lt.govilnius.domain.reservation.LiveMeet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface LiveMeetRepository extends JpaRepository<LiveMeet, Long>,
        CrudRepository<LiveMeet, Long> {
}
