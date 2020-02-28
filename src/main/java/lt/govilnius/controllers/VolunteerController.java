package lt.govilnius.controllers;

import com.google.common.collect.ImmutableList;
import lt.govilnius.domain.reservation.*;
import lt.govilnius.facadeService.reservation.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/volunteer-management")
public class VolunteerController {

    @Autowired
    private VolunteerService volunteerService;

    @RequestMapping(value = { "", "/" })
    public String index(Model model) {
        model.addAttribute("activePage", "volunteers");
        model.addAttribute("volunteers", this.volunteerService.getAll());
        return "volunteer/index";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addVolunteerForm(Volunteer volunteer, Model model) {
        model.addAttribute("activePage", "volunteers");
        model.addAttribute("availableLanguages", languages());
        model.addAttribute("volunteerDto", new VolunteerDto());
        return "volunteer/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addVolunteer(@Valid VolunteerDto volunteerDto,
                               BindingResult bindingResult, Model model) {
        model.addAttribute("activePage", "volunteers");
        model.addAttribute("availableLanguages", languages());
        if (bindingResult == null || bindingResult.hasErrors()) {
            return "volunteer/add";
        }
        volunteerDto.setActive(true);
        Optional<Volunteer> optional = volunteerService.create(volunteerDto);
        return optional.isPresent() ?
                "redirect:/volunteer-management" :
                "volunteer/add";
    }

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String viewVolunteer(@RequestParam(name = "id") String id, Model model) {
        final Volunteer volunteer = this.volunteerService.get(Long.parseLong(id)).get();
        model.addAttribute("volunteer", volunteer);
        model.addAttribute("languages", checkedLanguages(volunteer.getLanguages()));
        model.addAttribute("activePage", "volunteers");
        return "volunteer/view";
    }

    private List<CheckedLanguage> checkedLanguages(Set<VolunteerLanguage> languages) {
        List<CheckedLanguage> checkedLanguages = languages()
                .stream()
                .map(e -> new CheckedLanguage(e, false))
                .collect(Collectors.toList());
        for (int i = 0; i < checkedLanguages.size(); i++) {
            for (Language checked : languages
                    .stream()
                    .map(VolunteerLanguage::getLanguage)
                    .collect(Collectors.toList())) {
                if (checked.getName().equals(checkedLanguages.get(i).getLanguage().getName())) {
                    final CheckedLanguage checkedLanguage = checkedLanguages.get(i);
                    checkedLanguage.setChecked(true);
                    checkedLanguages.set(i, checkedLanguage);
                }
            }
        }
        return checkedLanguages;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editVolunteer(@RequestParam(name = "id") String id, Model model) {
        model.addAttribute("activePage", "volunteers");
        final Optional<Volunteer> volunteer = volunteerService.get(Long.parseLong(id));
        if (volunteer.isPresent()) {
            model.addAttribute("volunteerDto", new VolunteerDto(volunteer.get()));
            model.addAttribute("availableLanguages", checkedLanguages(volunteer.get().getLanguages()));
            return "volunteer/edit";
        } else {
            return "redirect:/volunteer-management";
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateVolunteer(VolunteerDto volunteerDto) {
        volunteerService.edit(volunteerDto.getId(), volunteerDto);
        return "redirect:/volunteer-management/view?id=" + volunteerDto.getId();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteVolunteer(@RequestParam(name = "id") String id) {
        this.volunteerService.delete(Long.parseLong(id));
        return "redirect:/volunteer-management";
    }

    private List<Language> languages() {
        return ImmutableList
                .<Language>builder()
                .add(Language.ENGLISH)
                .add(Language.RUSSIAN)
                .build();
    }
}
