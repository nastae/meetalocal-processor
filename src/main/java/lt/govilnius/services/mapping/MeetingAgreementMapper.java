package lt.govilnius.services.mapping;

import lt.govilnius.models.MeetingAgreement;
import lt.govilnius.repository.entity.MeetingAgreementEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface MeetingAgreementMapper {

    MeetingAgreement fromEntity(MeetingAgreementEntity entity);
}
