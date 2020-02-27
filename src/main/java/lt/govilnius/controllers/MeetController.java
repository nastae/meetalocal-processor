package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.facadeService.reservation.MeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/meet-management")
public class MeetController {

    @Autowired
    private MeetService meetService;

    @RequestMapping(value = { "", "/" })
    public String index(Model model) {
        model.addAttribute("activePage", "meets");
        model.addAttribute("meets", this.meetService.getAll());
        return "meet/index";
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewMeet(@RequestParam(name = "id") String id, Model model) {
        final Meet meet = this.meetService.get(Long.parseLong(id)).get();
        model.addAttribute("meet", meet);
        model.addAttribute("activePage", "meets");
        return "meet/view";
    }
}
