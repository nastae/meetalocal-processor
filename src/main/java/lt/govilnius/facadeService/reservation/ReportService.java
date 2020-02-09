package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.Report;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.repository.reservation.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Optional<Report> create(Meet meet, Volunteer volunteer, String comment) {
        return Optional.ofNullable(add(meet, volunteer, comment));
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
