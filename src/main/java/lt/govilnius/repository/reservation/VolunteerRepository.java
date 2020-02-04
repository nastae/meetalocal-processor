package lt.govilnius.repository.reservation;

import lt.govilnius.domain.reservation.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long>,
        CrudRepository<Volunteer, Long> {

    List<Volunteer> findByActive(Boolean active);
}
