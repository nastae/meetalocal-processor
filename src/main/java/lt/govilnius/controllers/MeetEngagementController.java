package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/engagement-management")
public class MeetEngagementController {

    @Autowired
    private MeetEngagementService meetEngagementService;

    @GetMapping("/engagements/meets/{id}")
    public ResponseEntity<?> getByMeet(@PathVariable("id") long id) {
        final List<MeetEngagement> meetEngagements = meetEngagementService.getByMeetId(id);
        return ResponseEntity.ok(meetEngagements);
    }

    @GetMapping("/engagements/volunteers/{id}")
    public ResponseEntity<?> getByVolunteer(@PathVariable("id") long id) {
        final List<MeetEngagement> meetEngagements = meetEngagementService.getByVolunteerId(id);
        return ResponseEntity.ok(meetEngagements);
    }
}
