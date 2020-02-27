package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.Evaluation;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.VolunteerActionService;
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
@RequestMapping("/volunteer-action-management")
public class VolunteerActionController {

    @Autowired
    private VolunteerActionService volunteerActionService;

    @Autowired
    private MeetEngagementService meetEngagementService;

    @GetMapping("/agreements")
    public String agree(@RequestParam(name = "token") String token) {
        final Optional<MeetEngagement> engagement = volunteerActionService.agree(token);
        return engagement.isPresent() ?
                "thanks-for-answer-en" :
                "error-en";
    }

    @GetMapping("/cancellations")
    public String cancel(@RequestParam(name = "token") String token) {
        final Optional<MeetEngagement> engagement = volunteerActionService.cancel(token);
        return engagement.isPresent() ?
                "thanks-for-answer-en" :
                "error-en";
    }

    @GetMapping("/engagements")
    public String edit(@RequestParam(name = "token") String token, Model model) {
        final Optional<MeetEngagement> meetEngagement = meetEngagementService.getByToken(token);
        if (meetEngagement.isPresent()) {
            MeetEngagement engagement = meetEngagement.get();
            model.addAttribute("token", token);
            model.addAttribute("time", engagement.getMeet().getTime().toString());
            model.addAttribute("tourist", engagement.getMeet().getName());
            return "edit-engagement";
        } else {
            return "error-en";
        }
    }

    @PostMapping("/engagements")
    public String edit(@RequestParam Map<String, String> params) {
        final Optional<MeetEngagement> engagement = volunteerActionService.editEngagement(params.get("token"), params.get("time") + ":00");
        return engagement.isPresent() ?
                "thanks-for-answer-en" :
                "error-en";
    }

    @GetMapping("/evaluations")
    public String evaluate(@RequestParam(name = "token") String token, Model model) {
        final Optional<MeetEngagement> meetEngagement = meetEngagementService.getByToken(token);
        if (meetEngagement.isPresent()) {
            MeetEngagement engagement = meetEngagement.get();
            model.addAttribute("token", token);
            return "volunteer-evaluation";
        } else {
            return "error-en";
        }
    }

    @PostMapping("/evaluations")
    public String evaluate(@RequestParam Map<String, String> params) {
        final Optional<Evaluation> engagement = volunteerActionService.evaluate(params.get("token"), params.get("comment"));
        return engagement.isPresent() ?
                "thanks-for-answer-en" :
                "error-en";
    }
}
