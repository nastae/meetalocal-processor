package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetCriteria;
import lt.govilnius.domain.reservation.Status;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class MeetSpecification implements Specification<Meet> {

    public static final String ANY = "ANY";

    private MeetCriteria meetCriteria;

    public MeetSpecification(MeetCriteria meetCriteria) {
        this.meetCriteria = meetCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Meet> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {

        Path<Status> status = root.get("status");
        Path<String> type = root.get("type");
        final List<Predicate> predicates = new ArrayList<Predicate>();
        if (meetCriteria.getStatus() != null) {
            if (!meetCriteria.getStatus().equals(ANY)) {
                predicates.add(cb.equal(status, Status.valueOf(meetCriteria.getStatus())));
            }
        }
        if (meetCriteria.getMeetType() != null) {
            if (!meetCriteria.getMeetType().equals(ANY)) {
                predicates.add(cb.equal(type, meetCriteria.getMeetType()));
            }
        }
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
