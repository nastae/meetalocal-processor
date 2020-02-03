package lt.govilnius.repository.reservation;

import lt.govilnius.domain.reservation.MeetLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetLanguageRepository extends JpaRepository<MeetLanguage, Long> {
}
