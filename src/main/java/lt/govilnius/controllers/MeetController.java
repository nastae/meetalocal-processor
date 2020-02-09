package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Report;
import lt.govilnius.facadeService.reservation.InteractionWithMeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/meet")
public class MeetController {

    @Autowired
    private InteractionWithMeetService interactionWithMeetService;

    @PostMapping(value = "/agreements",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> agree(@RequestParam Map<String, String> params) {
        final Optional<MeetEngagement> engagement = interactionWithMeetService.agree(params.get("token"));
        return engagement.isPresent() ?
                ResponseEntity.ok().build() :
                ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping(value = "/cancellations", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> cancel(@RequestParam Map<String, String> params) {
        final Optional<MeetEngagement> engagement = interactionWithMeetService.cancel(params.get("token"));
        return engagement.isPresent() ?
                ResponseEntity.ok().build() :
                ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping(value = "/changes", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> changeEngagement(@RequestParam Map<String, String> params) {
        final Optional<MeetEngagement> engagement = interactionWithMeetService.change(params.get("token"), params.get("time"));
        return engagement.isPresent() ?
                ResponseEntity.ok().build() :
                ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping(value = "/reports", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> report(@RequestParam Map<String, String> params) {
        final Optional<Report> report = interactionWithMeetService.report(params.get("token"), params.get("comment"));
        return report.isPresent() ?
                ResponseEntity.ok().build() :
                ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping(value = "/changes-after-addition", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> changeEngagementAfterAddition(@RequestParam Map<String, String> params) {
        final Optional<MeetEngagement> engagement = interactionWithMeetService.changeAfterAddition(params.get("token"), params.get("time"));
        return engagement.isPresent() ?
                ResponseEntity.ok().build() :
                ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping(value = "/selections", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> selectVolunteer(@RequestParam Map<String, String> params) {
        final Optional<Meet> meet = interactionWithMeetService.select(params.get("token"));
        return meet.isPresent() ?
                ResponseEntity.ok().build() :
                ResponseEntity.unprocessableEntity().build();
    }
}
