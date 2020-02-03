package lt.govilnius.repository.reservation;

import lt.govilnius.domain.reservation.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long>,
        CrudRepository<Volunteer, Long>,
        PagingAndSortingRepository<Volunteer, Long> {
}
