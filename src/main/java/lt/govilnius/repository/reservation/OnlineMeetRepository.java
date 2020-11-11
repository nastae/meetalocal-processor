package lt.govilnius.repository.reservation;

import lt.govilnius.domain.reservation.OnlineMeet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface OnlineMeetRepository extends JpaRepository<OnlineMeet, Long>,
        CrudRepository<OnlineMeet, Long> {
}
