package lt.govilnius.domainService.filter;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MeetEngagementFilter {

    public List<MeetEngagement> filterForMail(List<MeetEngagement> engagements, Meet meet) {
        return engagements
                .stream()
                .sorted((o1, o2) -> {
                    if (o1.getTime().equals(meet.getTime()))
                        return 1;
                    else if (meet.getTime().getTime() - o1.getTime().getTime() <= meet.getTime().getTime() - o2.getTime().getTime())
                        return 1;
                    else if (o1.getTime().equals(o2.getTime()))
                        return 0;
                    else
                        return -1;
                })
                .limit(5)
                .collect(Collectors.toList());
    }
}
