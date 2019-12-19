package lt.govilnius.services.mapping;

import lt.govilnius.models.Volunteer;
import lt.govilnius.repository.entity.VolunteerEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface VolunteerMapper {

    Volunteer fromEntity(VolunteerEntity entity);
}
