package lt.govilnius.repository.reservation;

import lt.govilnius.domain.reservation.VolunteerLanguage;
import lt.govilnius.domain.reservation.VolunteerPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface VolunteerPurposeRepository extends JpaRepository<VolunteerPurpose, Long> {
}
