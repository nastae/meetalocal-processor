package lt.govilnius.processor;

import lt.govilnius.models.MeetingForm;
import lt.govilnius.models.Volunteer;
import lt.govilnius.repository.VolunteerRepository;
import lt.govilnius.services.mapping.VolunteerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VolunteerFilter {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private VolunteerMapper volunteerMapper;

    public List<Volunteer> filterByForm(MeetingForm form) {
        return volunteerRepository
                .findAll()
                .stream()
                .map(v -> volunteerMapper.fromEntity(v))
                .collect(Collectors.toList());
    }
}
