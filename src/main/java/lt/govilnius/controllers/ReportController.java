package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.Report;
import lt.govilnius.facadeService.reservation.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/report-management")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/reports/meets/{id}")
    public ResponseEntity<?> getByMeet(@PathVariable("id") long id) {
        final List<Report> reports = reportService.getByMeetId(id);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/reports/volunteers/{id}")
    public ResponseEntity<?> getByVolunteer(@PathVariable("id") long id) {
        final List<Report> reports = reportService.getByVolunteerId(id);
        return ResponseEntity.ok(reports);
    }
}
