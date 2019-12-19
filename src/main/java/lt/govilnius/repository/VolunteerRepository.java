package lt.govilnius.repository;

import lt.govilnius.repository.entity.VolunteerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerEntity, Long>,
        CrudRepository<VolunteerEntity, Long>,
        PagingAndSortingRepository<VolunteerEntity, Long> {

//    List<VolunteerEntity> findBy
}
