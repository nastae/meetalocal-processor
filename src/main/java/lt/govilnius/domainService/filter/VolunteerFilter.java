package lt.govilnius.domainService.filter;

import lt.govilnius.domain.reservation.*;
import lt.govilnius.domainService.time.DateUtils;
import lt.govilnius.facadeService.reservation.VolunteerService;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Component
public class VolunteerFilter {

    @Autowired
    private VolunteerService volunteerService;

    public List<Volunteer> filterByMeet(Meet meet, MeetType meetType) {
        return volunteerService
                .findByMeetType(meetType)
                .stream()
                .filter(v -> existJointLanguage(meet.getLanguages(), v.getLanguages()))
                .peek(v -> System.out.println(isBetweenAgeGroup(meet.getMeetAgeGroups(), DateUtils.yearsFromNow(v.getDateOfBirth()))))
                .filter(v -> isBetweenAgeGroup(meet.getMeetAgeGroups(), DateUtils.yearsFromNow(v.getDateOfBirth())))
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
