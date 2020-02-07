package lt.govilnius.facadeService.reservation;

import io.atlassian.fugue.Either;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.Report;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.repository.reservation.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Either<Exception, Report> create(Meet meet, Volunteer volunteer, String comment) {
        try {
            return Either.right(add(meet, volunteer, comment));
        } catch (RuntimeException e) {
            return Either.left(new RuntimeException("Fail to create a meet engagement entity"));
        }
    }

    private Report add(Meet meet, Volunteer volunteer, String comment) {
        final Report entity = new Report();
        entity.setMeet(meet);
        entity.setVolunteer(volunteer);
        entity.setComment(comment);
        return reportRepository.save(entity);
    }

    public List<Report> getAll() {
        return reportRepository.findAll();
    }

    public List<Report> getByMeetId(Long meetId) {
        return reportRepository.findByMeetId(meetId);
    }

    public List<Report> getByVolunteerId(Long volunteerId) {
        return reportRepository.findByVolunteerId(volunteerId);
    }
}
