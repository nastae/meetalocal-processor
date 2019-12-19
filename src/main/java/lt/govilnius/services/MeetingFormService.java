package lt.govilnius.services;

import io.atlassian.fugue.Either;
import lt.govilnius.models.MeetingForm;
import lt.govilnius.models.Status;
import lt.govilnius.repository.MeetingFormRepository;
import lt.govilnius.repository.VolunteerRepository;
import lt.govilnius.repository.entity.MeetingFormEntity;
import lt.govilnius.services.mapping.MeetingFormMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class MeetingFormService {

    @Autowired
    private MeetingFormRepository repository;

    @Autowired
    private MeetingFormMapper meetingFormMapper;

    @Autowired
    private VolunteerRepository volunteerRepository;

    public List<MeetingFormEntity> getMeetingForms() {
        return repository.findAll();
    }

    public Either<Error, MeetingForm> create(MeetingForm form) {
        return Either.right(meetingFormMapper.fromEntity(add(form)));
    }

    public Either<Error, MeetingForm> addVolunteer(Long id, Long volunteerId) {
        final MeetingFormEntity entity = repository.findById(id).get();
        if (entity.getVolunteerEntity() == null)
            return Either.left(Error.ELEMENT_IS_SET);
        entity.setVolunteerEntity(volunteerRepository.findById(volunteerId).orElseThrow(RuntimeException::new));
        return Either.right(meetingFormMapper.fromEntity(repository.save(entity)));
    }

    private MeetingFormEntity add(MeetingForm form) {
        final MeetingFormEntity entity = new MeetingFormEntity();
        entity.setEmail(form.getEmail());
        entity.setPhoneNumber(form.getPhoneNumber());
        entity.setNameAndLastname(form.getNameAndLastname());
        entity.setFromCountry(form.getFromCountry());
        entity.setMeetingDate(form.getMeetingDate());
        entity.setStatus(Status.NEW);
//        entity.setHobbies(mapper.toEntities(form.getHobbies()));
        entity.setPreferences(form.getPreferences());
        return repository.save(entity);
    }

    public List<MeetingForm> getAll(Pageable pageRequest) {
        return repository.findAll(pageRequest)
                .getContent()
                .stream()
                .map(e -> meetingFormMapper.fromEntity(e))
                .collect(toList());
    }

    public Optional<MeetingForm> get(Long id) {
        return repository.findById(id)
                .map(e -> meetingFormMapper.fromEntity(e));
    }

    public Optional<MeetingForm> delete(Long id) {
        return repository.findById(id)
                .map(this::deleteEntity);
    }

    private MeetingForm deleteEntity(MeetingFormEntity entity) {
        repository.delete(entity);
        return meetingFormMapper.fromEntity(entity);
    }

    public Either<Error, MeetingForm> edit(Long id, MeetingForm form) {
        final Optional<MeetingFormEntity> entity = repository.findById(id);
        return entity.<Either<Error, MeetingForm>>map(
                meetingFormEntity -> Either.right(meetingFormMapper.fromEntity(updateEntity(meetingFormEntity, form))))
                .orElseGet(() -> Either.left(Error.TASK_NOT_FOUND));
    }

    private MeetingFormEntity updateEntity(MeetingFormEntity entity, MeetingForm newData) {
        final MeetingFormEntity updatedEntity = meetingFormMapper.toEntity(newData);
        updatedEntity.setId(entity.getId());
        updatedEntity.setStatus(newData.getStatus());
        return repository.save(updatedEntity);
    }

    public List<MeetingFormEntity> findByStatus(Status status) {
        return repository.findByStatus(status);
    }

    public MeetingForm fromEntity(MeetingFormEntity entity) {
        return meetingFormMapper.fromEntity(entity);
    }

    public MeetingFormEntity toEntity(MeetingForm form) {
        return meetingFormMapper.toEntity(form);
    }
}
