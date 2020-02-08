package lt.govilnius;

import com.google.common.collect.ImmutableList;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domainService.filter.MeetEngagementFilter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Time;
import java.util.List;

import static lt.govilnius.EmailSenderTest.sampleMeet;

@RunWith(MockitoJUnitRunner.class)
public class MeetEngagementFilterTest {

    @InjectMocks
    private MeetEngagementFilter meetEngagementFilter;

    @Test
    public void filterForMail_MeetEngagements_ShouldReturnOnly5() {
        Meet meet = sampleMeet();
        meet.setTime(new Time(10, 10, 10));
        List<MeetEngagement> meetEngagements = ImmutableList.of(
                new MeetEngagement(meet, null, new Time(10, 10, 10)),
                new MeetEngagement(meet, null, new Time(10, 10, 10)),
                new MeetEngagement(meet, null, new Time(10, 10, 10)),
                new MeetEngagement(meet, null, new Time(10, 10, 10)),
                new MeetEngagement(meet, null, new Time(10, 10, 10)),
                new MeetEngagement(meet, null, new Time(10, 10, 10))
        );
        Assert.assertEquals(meetEngagements.size(), 6);
        meetEngagements = meetEngagementFilter.filterForMail(meetEngagements, meet);
        Assert.assertEquals(meetEngagements.size(), 5);
    }

    @Test
    public void filterForMail_MeetEngagements_ShouldReturnOrderByTime() {
        Meet meet = sampleMeet();
        meet.setTime(new Time(10, 10, 10));
        List<MeetEngagement> meetEngagements = ImmutableList.of(
                new MeetEngagement(meet, null, new Time(10, 10, 10)),
                new MeetEngagement(meet, null, new Time(10, 10, 10)),
                new MeetEngagement(meet, null, new Time(10, 14, 10)),
                new MeetEngagement(meet, null, new Time(10, 13, 10)),
                new MeetEngagement(meet, null, new Time(10, 12, 10)),
                new MeetEngagement(meet, null, new Time(10, 15, 10))
        );
        Assert.assertEquals(meetEngagements.size(), 6);
        meetEngagements = meetEngagementFilter.filterForMail(meetEngagements, meet);
        Assert.assertEquals(meetEngagements.size(), 5);
        Assert.assertEquals(meetEngagements.get(0).getTime(), new Time(10, 10, 10));
        Assert.assertEquals(meetEngagements.get(1).getTime(), new Time(10, 10, 10));
        Assert.assertEquals(meetEngagements.get(2).getTime(), new Time(10, 12, 10));
        Assert.assertEquals(meetEngagements.get(3).getTime(), new Time(10, 13, 10));
        Assert.assertEquals(meetEngagements.get(4).getTime(), new Time(10, 14, 10));

    }
}
