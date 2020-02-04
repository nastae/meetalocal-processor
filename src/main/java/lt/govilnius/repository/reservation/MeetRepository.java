package lt.govilnius.repository.reservation;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetRepository extends JpaRepository<Meet, Long>,
        CrudRepository<Meet, Long> {

    List<Meet> findByStatus(Status status);
}
