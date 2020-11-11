package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetCriteria;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.repository.reservation.MeetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class MeetService {

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private MeetStatusService meetStatusService;

    @Autowired
    private MeetLanguageService meetLanguageService;

    @Autowired
    private MeetAgeGroupService meetAgeGroupService;

    public Meet setVolunteer(Meet meet, Volunteer volunteer) {
        meet.setVolunteer(volunteer);
        return meetRepository.save(meet);
    }

    public Meet setFreezed(Meet meet, boolean freezed) {
        meet.setFreezed(freezed);
        return meetRepository.save(meet);
    }

    public List<Meet> getAll() {
        return meetRepository.findAll();
    }

    public Optional<Meet> get(Long id) {
        return meetRepository.findById(id);
    }

    public List<Meet> getSortedByIdAll(List<Meet> meets) {
        meets.sort((e1, e2) -> (int) (e2.getId() - e1.getId()));
        return meets;
    }

    public Optional<Meet> edit(Long id, Meet meet) {
        final Optional<Meet> entity = meetRepository.findById(id);
        return entity.map(e -> updateEntity(e, meet));
    }

    private Meet updateEntity(Meet entity, Meet newData) {
        if (!newData.getStatus().equals(entity.getStatus())) {
            meetStatusService.create(entity, entity.getStatus());
        }
        entity.setStatus(newData.getStatus());
        entity.setChangedAt(new Timestamp(System.currentTimeMillis()));
        entity = meetRepository.save(entity);
        return entity;
    }

    public List<Meet> findByStatus(Status status) {
        return meetRepository.findByStatus(status);
    }

    public List<Meet> findByCriteria(MeetSpecification meetSpecification) {
        return meetRepository.findAll(meetSpecification);
    }
}
