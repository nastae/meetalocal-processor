package lt.govilnius.domainService.filter;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Component
public class VolunteerFilter {

    @Autowired
    private VolunteerRepository volunteerRepository;

    public List<Volunteer> filterByMeet(Meet meet) {
        return volunteerRepository
                .findAll()
                .stream()
                .filter(v -> existJointLanguage(meet.getLanguages(), v.getLanguages()))
                .filter(v -> isBetweenAgeGroup(meet.getMeetAgeGroups(), v.getAge()))
                .collect(toList());
    }

    private boolean existJointLanguage(Set<MeetLanguage> meetLanguages, Set<VolunteerLanguage> volunteerLanguages) {
        for (MeetLanguage meetLanguage: meetLanguages) {
            for (VolunteerLanguage volunteerLanguage: volunteerLanguages) {
                if (meetLanguage.getLanguage().getName().equals(volunteerLanguage.getLanguage().getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBetweenAgeGroup(Set<MeetAgeGroup> ageGroups, Long age) {
        for (MeetAgeGroup meetAgeGroup : ageGroups) {
            final AgeGroup ageGroup = meetAgeGroup.getAgeGroup();
            if (ageGroup.getFrom() <= age && age <= ageGroup.getTo())
                return true;
        }
        return false;
    }
}
