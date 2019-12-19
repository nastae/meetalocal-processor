package lt.govilnius.repository;

import lt.govilnius.models.Status;
import lt.govilnius.repository.entity.MeetingFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingFormRepository extends JpaRepository<MeetingFormEntity, Long>,
        CrudRepository<MeetingFormEntity, Long>,
        PagingAndSortingRepository<MeetingFormEntity, Long> {

    List<MeetingFormEntity> findByStatus(Status status);
}
