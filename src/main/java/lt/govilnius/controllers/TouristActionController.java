package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.AgeGroup;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tourist-action-management")
public class TouristActionController {

    @Autowired
    private MeetService meetService;

    @Autowired
    private TouristActionService touristActionService;

    @Autowired
    private MeetEngagementService meetEngagementService;

    @GetMapping("/meets")
    public String edit(@RequestParam(name = "meet") String meetId, Model model) {
        try {
            final Optional<Meet> meetOptional = meetService.get(Long.parseLong(meetId));
            if (touristActionService.isFreezed(meetOptional))
                return "currently-selected-en";
            if (meetOptional.isPresent()) {
                model.addAttribute("meet", meetId);
                return "edit-meet";
            } else {
                return "run-out-of-time-en";
            }
        } catch (RuntimeException ex) {
            return "run-out-of-time-en";
        }
    }

    @PostMapping("/meets")
    public String edit(@RequestParam Map<String, String> params) {
        List<AgeGroup> ageGroups = Arrays.stream(params.get("ageGroup").substring(1, params.get("ageGroup").length()-1).split(","))
                .map(AgeGroup::fromString)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        final Optional<Meet> meet = touristActionService.editMeet(params.get("meet"), ageGroups);
        if (touristActionService.isFreezed(meet))
            return "currently-selected-en";
        return meet.isPresent() ?
                "thanks-for-answer-en" :
                "run-out-of-time-en";
    }

    @GetMapping("/selections")
    public String select(@RequestParam(name = "token") String token) {
        if (touristActionService.isFreezed(token))
            return "currently-selected-en";
        final Optional<Meet> meet = touristActionService.select(token);
        if (meet.isPresent()) {
            return "thanks-for-answer-en";
        } else {
            return "run-out-of-time-en";
        }
    }

    @GetMapping("/evaluations")
    public String evaluate(@RequestParam(name = "token") String token, Model model) {
        if (touristActionService.isFreezed(token))
            return "currently-selected-en";
        final Optional<MeetEngagement> meetEngagement = meetEngagementService.getByToken(token);
        if (meetEngagement.isPresent()) {
            model.addAttribute("token", token);
            return "tourist-evaluation";
        } else {
            return "run-out-of-time-en";
        }
    }

    @PostMapping("/evaluations")
    public String evaluate(@RequestParam Map<String, String> params) {
        final String token = params.get("token");
        if (touristActionService.isFreezed(token))
            return "currently-selected-en";
        final Optional<Evaluation> engagement = touristActionService.evaluate(token, params.get("comment"));
        return engagement.isPresent() ?
                "thanks-for-answer-en" :
                "run-out-of-time-en";
    }
}
