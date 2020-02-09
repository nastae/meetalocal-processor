package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.MeetService;
import lt.govilnius.facadeService.reservation.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MeetEngagementService meetEngagementService;

    @Autowired
    private MeetService meetService;

    @Autowired
    private VolunteerService volunteerService;

    @GetMapping("/agreements")
    public String agree(@RequestParam(name = "token") String token, Model model) {
        final Optional<MeetEngagement> meetEngagement = meetEngagementService.getByToken(token);
        if (meetEngagement.isPresent()) {
            MeetEngagement engagement = meetEngagement.get();
            model.addAttribute("token", token);
            model.addAttribute("tourist", engagement.getMeet().getName());
            return "agree-engagement";
        } else {
            return "error";
        }
    }

    @GetMapping("/cancellations")
    public String cancel(@RequestParam(name = "token") String token, Model model) {
        final Optional<MeetEngagement> meetEngagement = meetEngagementService.getByToken(token);
        if (meetEngagement.isPresent()) {
            MeetEngagement engagement = meetEngagement.get();
            model.addAttribute("token", token);
            model.addAttribute("tourist", engagement.getMeet().getName());
            return "cancel-engagement";
        } else {
            return "error";
        }
    }

    @GetMapping("/engagements-changes")
    public String change(@RequestParam(name = "token") String token, Model model) {
        final Optional<MeetEngagement> meetEngagement = meetEngagementService.getByToken(token);
        if (meetEngagement.isPresent()) {
            MeetEngagement engagement = meetEngagement.get();
            model.addAttribute("token", token);
            model.addAttribute("time", engagement.getMeet().getTime().toString());
            model.addAttribute("tourist", engagement.getMeet().getName());
            return "change-time-engagement";
        } else {
            return "error";
        }
    }

    @GetMapping("/reports")
    public String report(@RequestParam(name = "token") String token, Model model) {
        final Optional<MeetEngagement> meetEngagement = meetEngagementService.getByToken(token);
        if (meetEngagement.isPresent()) {
            MeetEngagement engagement = meetEngagement.get();
            model.addAttribute("token", token);
            model.addAttribute("tourist", engagement.getMeet().getName());
            return "report-tourist";
        } else {
            return "error";
        }
    }

    @GetMapping("/meets-changes")
    public String changeMeetsAfterAddition(@RequestParam(name = "meet") String meetId, Model model) {
        final Optional<Meet> meetOptional = meetService.get(Long.parseLong(meetId));
        if (meetOptional.isPresent()) {
            Meet meet = meetOptional.get();
            model.addAttribute("meet", meetId);
            return "change-time-meet";
        } else {
            return "error";
        }
    }

    @GetMapping("/selections")
    public String select(@RequestParam(name = "token") String token, Model model) {
        final Optional<MeetEngagement> meetEngagement = meetEngagementService.getByToken(token);
        if (meetEngagement.isPresent()) {
            MeetEngagement engagement = meetEngagement.get();
            model.addAttribute("token", token);
            model.addAttribute("volunteer", engagement.getVolunteer().getName());
            return "select-volunteer";
        } else {
            return "error";
        }
    }
}
