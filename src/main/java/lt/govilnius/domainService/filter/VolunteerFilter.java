package lt.govilnius.domainService.filter;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class VolunteerFilter {

    @Autowired
    private VolunteerRepository volunteerRepository;

    public List<Volunteer> filterByForm(Meet form) {
        return volunteerRepository
                .findAll()
                .stream()
                .filter((v) -> v.getGender().equals(form.getGender()))
                .collect(toList());
    }
}
