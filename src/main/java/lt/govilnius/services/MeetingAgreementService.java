package lt.govilnius.services;

import io.atlassian.fugue.Either;
import lt.govilnius.exception.NotFoundElementException;
import lt.govilnius.models.MeetingAgreement;
import lt.govilnius.repository.MeetingAgreementRepository;
import lt.govilnius.repository.MeetingFormRepository;
import lt.govilnius.repository.VolunteerRepository;
import lt.govilnius.repository.entity.MeetingAgreementEntity;
import lt.govilnius.repository.entity.MeetingFormEntity;
import lt.govilnius.services.mapping.MeetingAgreementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MeetingAgreementService {

    @Autowired
    private MeetingFormRepository meetingFormRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private MeetingAgreementRepository meetingAgreementRepository;

    @Autowired
    private MeetingAgreementMapper meetingAgreementMapper;

    public MeetingAgreementService() {
    }

    public Either<Error, MeetingAgreement> create(MeetingAgreement agreement) {
        try {
            return Either.right(meetingAgreementMapper.fromEntity(add(agreement)));
        } catch (NotFoundElementException e) {
            return Either.left(Error.NOT_FOUND_ELEMENT);
        }
    }

    private MeetingAgreementEntity add(MeetingAgreement agreement) throws NotFoundElementException {
        final MeetingAgreementEntity entity = new MeetingAgreementEntity();
        entity.setVolunteerEntity(volunteerRepository.findById(agreement.getVolunteerId())
                .orElseThrow(() -> new NotFoundElementException("Not found volunteer with id " + agreement.getVolunteerId())));
        entity.setMeetingForm(meetingFormRepository.findById(agreement.getMeetingFormId())
                .orElseThrow(() -> new NotFoundElementException("Not found meeting form with id " + agreement.getMeetingFormId())));
        entity.setAgreed(agreement.getAgreed());
        return meetingAgreementRepository.save(entity);
    }

    public Either<Error, MeetingAgreement> edit(Long id, Boolean isAgreed) {
        final Optional<MeetingAgreementEntity> entity = meetingAgreementRepository.findById(id);
        return entity.<Either<Error, MeetingAgreement>>map(
                agreementEntity -> Either.right(meetingAgreementMapper.fromEntity(updateEntity(agreementEntity, isAgreed))))
                .orElseGet(() -> Either.left(Error.TASK_NOT_FOUND));
    }

    private MeetingAgreementEntity updateEntity(MeetingAgreementEntity entity, Boolean isAgreed) {
        entity.setAgreed(isAgreed);
        return entity;
    }

    public List<MeetingAgreementEntity> findByIsAgreedAndMeetingForm(Boolean isAgreed, MeetingFormEntity formEntity) {
        return meetingAgreementRepository.findByIsAgreedAndMeetingForm(isAgreed, formEntity);
    }

    public List<MeetingAgreement> findAgreedAgreements(MeetingFormEntity formEntity) {
        return meetingAgreementRepository.findByIsAgreedAndMeetingForm(true, formEntity)
                .stream()
                .map(e -> meetingAgreementMapper.fromEntity(e))
                .collect(Collectors.toList());
    }
}
