package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/engagement-management")
public class MeetEngagementController {

    @Autowired
    private MeetEngagementService meetEngagementService;

    @RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
    public String index(@RequestParam("id") String id, Model model) {
        model.addAttribute("engagements", meetEngagementService.getByMeetId(Long.parseLong(id)));
        return "engagement/index";
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewMeet(@RequestParam(name = "token") String token, Model model) {
        final MeetEngagement engagement = meetEngagementService.getByToken(token).get();
        model.addAttribute("engagement", engagement);
        model.addAttribute("activePage", "meets");
        return "meet/view";
    }
}
