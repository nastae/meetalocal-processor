package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Report;
import lt.govilnius.facadeService.reservation.InteractionWithMeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/meet")
public class MeetStatusController {

    @Autowired
    private InteractionWithMeetService interactionWithMeetService;

    @PostMapping("/agreements")
    public String agree(@RequestParam Map<String, String> params) {
        final Optional<MeetEngagement> engagement = interactionWithMeetService.agree(params.get("token"));
        return engagement.isPresent() ?
                "thanks-for-answer" :
                "error";
    }

    @PostMapping("/cancellations")
    public String cancel(@RequestParam Map<String, String> params) {
        final Optional<MeetEngagement> engagement = interactionWithMeetService.cancel(params.get("token"));
        return engagement.isPresent() ?
                "thanks-for-answer" :
                "error";
    }

    @PostMapping("/engagements-changes")
    public String change(@RequestParam Map<String, String> params) {
        final Optional<MeetEngagement> engagement = interactionWithMeetService.changeEngagement(params.get("token"), params.get("time"));
        return engagement.isPresent() ?
                "thanks-for-answer" :
                "error";
    }

    @PostMapping("/reports")
    public String report(@RequestParam Map<String, String> params) {
        final Optional<Report> report = interactionWithMeetService.report(params.get("token"), params.get("comment"));
        return report.isPresent() ?
                "thanks-for-answer" :
                "error";
    }

    @PostMapping("/meets-changes")
    public String changeMeetsAfterAddition(@RequestParam Map<String, String> params) {
        final Optional<Meet> meet = interactionWithMeetService.changeMeetAfterAddition(params.get("meet"), params.get("time"));
        return meet.isPresent() ?
                "thanks-for-answer" :
                "error";
    }

    @PostMapping("/selections")
    public String select(@RequestParam Map<String, String> params) {
        final Optional<Meet> meet = interactionWithMeetService.select(params.get("token"));
        return meet.isPresent() ?
                "thanks-for-answer" :
                "error";
    }
}
