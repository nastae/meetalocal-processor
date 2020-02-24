package lt.govilnius.repository.reservation;

import lt.govilnius.domain.reservation.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long>  {
}
