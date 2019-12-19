package lt.govilnius.repository;

import lt.govilnius.repository.entity.MeetingAgreementEntity;
import lt.govilnius.repository.entity.MeetingFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingAgreementRepository extends JpaRepository<MeetingAgreementEntity, Long>,
        CrudRepository<MeetingAgreementEntity, Long>,
        PagingAndSortingRepository<MeetingAgreementEntity, Long> {

    List<MeetingAgreementEntity> findByIsAgreedAndMeetingForm(Boolean isAgreed, MeetingFormEntity meetingForm);
}