package lt.govilnius.services.mapping;

import lt.govilnius.models.MeetingForm;
import lt.govilnius.repository.entity.MeetingFormEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface MeetingFormMapper {
    MeetingForm fromEntity(MeetingFormEntity entity);
    MeetingFormEntity toEntity(MeetingForm form);
}
