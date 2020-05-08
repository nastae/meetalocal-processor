package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.Evaluation;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.MeetService;
import lt.govilnius.facadeService.reservation.TouristActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/tourist-action-management")
public class TouristActionController {

    @Autowired
    private MeetService meetService;

    @Autowired
    private TouristActionService touristActionService;

    @Autowired
    private MeetEngagementService meetEngagementService;

    @GetMapping("/selections")
    public String select(@RequestParam(name = "token") String token) {
        final Optional<Meet> meet = touristActionService.select(token);
        if (touristActionService.isFreezed(token))
            return "currently-selected-en";
        if (meet.isPresent()) {
            return "thanks-for-answer-en";
        } else {
            return "run-out-of-time-en";
        }
    }

    @GetMapping("/evaluations")
    public String evaluate(@RequestParam(name = "token") String token, Model model) {
        final Optional<MeetEngagement> meetEngagement = meetEngagementService.getByToken(token);
        if (touristActionService.isFreezed(token))
            return "currently-selected-en";
        if (meetEngagement.isPresent()) {
            model.addAttribute("token", token);
            return "tourist-evaluation";
        } else {
            return "currently-selected-en";
        }
    }

    @PostMapping("/evaluations")
    public String evaluate(@RequestParam Map<String, String> params) {
        String token = params.get("token");
        if (touristActionService.isFreezed(token))
            return "currently-selected-en";
        final Optional<Evaluation> engagement = touristActionService.evaluate(token, params.get("comment"));
        return engagement.isPresent() ?
                "thanks-for-answer-en" :
                "currently-selected-en";
    }
}
