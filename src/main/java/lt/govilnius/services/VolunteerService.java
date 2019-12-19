package lt.govilnius.services;

import lt.govilnius.models.MeetingAgreement;
import lt.govilnius.models.Volunteer;
import lt.govilnius.repository.VolunteerRepository;
import lt.govilnius.repository.entity.VolunteerEntity;
import lt.govilnius.services.mapping.VolunteerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private VolunteerMapper volunteerMapper;

    public List<Volunteer> findAgreementVolunteers(List<MeetingAgreement> agreements) {
        return agreements.stream()
                .map(a -> volunteerRepository.findById(a.getVolunteerId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(a -> volunteerMapper.fromEntity(a))
                .collect(Collectors.toList());
    }

    public Volunteer fromEntity(VolunteerEntity entity) {
        return volunteerMapper.fromEntity(entity);
    }
}
