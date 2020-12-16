package lt.govilnius.controllers;

import com.google.common.collect.ImmutableList;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetCriteria;
import lt.govilnius.domain.reservation.MeetType;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.facadeService.reservation.MeetService;
import lt.govilnius.facadeService.reservation.MeetSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static lt.govilnius.facadeService.reservation.MeetSpecification.ANY;

@Controller
@RequestMapping("/meet-management")
public class MeetController {

    @Autowired
    private MeetService meetService;

    @RequestMapping(value = { "", "/" })
    public String index(Model model, MeetCriteria meetCriteria) {
        meetCriteria = meetCriteria.getMeetType() == null ? new MeetCriteria(ANY, ANY) : meetCriteria;
        MeetSpecification meetSpecification = new MeetSpecification(meetCriteria);
        model.addAttribute("activePage", "meets");
        model.addAttribute("meets", meetService.getSortedByIdAll(meetService.findByCriteria(meetSpecification)));
        model.addAttribute("criteria", meetCriteria);
        model.addAttribute("meetTypes", meetTypes());
        model.addAttribute("statuses", statuses());
        return "meet/index";
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewMeet(@RequestParam(name = "id") String id, Model model) {
        final Meet meet = this.meetService.get(Long.parseLong(id)).get();
        model.addAttribute("meet", meet);
        model.addAttribute("activePage", "meets");
        return "meet/view";
    }

    private List<String> meetTypes() {
        return ImmutableList
                .<String>builder()
                .add(MeetType.LIVE.name())
                .add(MeetType.ONLINE.name())
                .add(ANY)
                .build();
    }

    private List<String> statuses() {
        return ImmutableList
                .<String>builder()
                .add(Status.NEW.name())
                .add(Status.SENT_VOLUNTEER_REQUEST.name())
                .add(Status.SENT_TOURIST_REQUEST.name())
                .add(Status.CANCELED.name())
                .add(Status.AGREED.name())
                .add(Status.FINISHED.name())
                .add(Status.ERROR.name())
                .add(ANY)
                .build();
    }
}
