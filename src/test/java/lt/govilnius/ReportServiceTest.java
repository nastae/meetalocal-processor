package lt.govilnius;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.Report;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.facadeService.reservation.ReportService;
import lt.govilnius.repository.reservation.MeetRepository;
import lt.govilnius.repository.reservation.ReportRepository;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static lt.govilnius.EmailSenderTest.sampleVolunteer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @After
    public void cleanEachTest() {
        reportRepository.findAll().forEach(volunteer -> reportRepository.delete(volunteer));
        meetRepository.findAll().forEach(meet -> meetRepository.delete(meet));
        volunteerRepository.findAll().forEach(volunteer -> volunteerRepository.delete(volunteer));
    }

    @Test
    public void create_Volunteer_ShouldBeCreated() {
        Volunteer volunteer = volunteerRepository.save(sampleVolunteer());
        Meet meet = meetRepository.save(sampleMeet());
        Report report = reportService.create(meet, volunteer, "Comment").get();
        Assert.assertEquals(report.getMeet().getId(), meet.getId());
        Assert.assertEquals(report.getVolunteer().getId(), volunteer.getId());
        Assert.assertEquals(report.getComment(), "Comment");
    }

    @Test
    public void getAll_MeetEngagements_ShouldGet() {
        Volunteer volunteer = volunteerRepository.save(sampleVolunteer());
        Meet meet = meetRepository.save(sampleMeet());
        reportService.create(meet, volunteer, "Comment");
        List<Report> reports = reportService.getAll();
        Assert.assertEquals(reports.size(), 1);
    }

    @Test
    public void getByMeetId_MeetEngagements_ShouldGet() {
        Volunteer volunteer = volunteerRepository.save(sampleVolunteer());
        Meet meet = meetRepository.save(sampleMeet());
        reportService.create(meet, volunteer, "Comment");
        reportService.create(meet, volunteer, "Comment");
        List<Report> reports = reportService.getByMeetId(meet.getId());
        Assert.assertEquals(reports.size(), 2);
    }

    @Test
    public void getByVolunteerId_MeetEngagements_ShouldGet() {
        Volunteer volunteer = volunteerRepository.save(sampleVolunteer());
        Meet meet = meetRepository.save(sampleMeet());
        reportService.create(meet, volunteer, "Comment");
        reportService.create(meet, volunteer, "Comment");
        reportService.create(meet, volunteer, "Comment");
        List<Report> reports = reportService.getByMeetId(meet.getId());
        Assert.assertEquals(reports.size(), 3);
    }
}
