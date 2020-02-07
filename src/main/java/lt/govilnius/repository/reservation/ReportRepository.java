package lt.govilnius.repository.reservation;

import lt.govilnius.domain.reservation.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long>,
        CrudRepository<Report, Long> {

    List<Report> findByMeetId(Long id);
    List<Report> findByVolunteerId(Long id);
}
