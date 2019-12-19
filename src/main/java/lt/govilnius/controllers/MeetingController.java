package lt.govilnius.controllers;

import lt.govilnius.mail.MeetingMailService;
import lt.govilnius.models.MeetingForm;
import lt.govilnius.repository.MeetingFormRepository;
import lt.govilnius.services.MeetingFormService;
import lt.govilnius.services.mapping.MeetingFormMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.unprocessableEntity;

@RestController
@RequestMapping
public class MeetingController {

    @Autowired
    private MeetingFormService meetingFormService;

    @Autowired
    private MeetingMailService meetingMailService;

    @Autowired
    private MeetingFormRepository meetingFormRepository;

    @Autowired
    private MeetingFormMapper meetingFormMapper;

    @PostMapping("/meetings")
    public ResponseEntity<?> createForm(@Valid @RequestBody MeetingForm form) {
        return meetingFormService.create(form)
                .fold(e -> unprocessableEntity().body(e),
                        ResponseEntity::ok);
    }

    @GetMapping("/meetings/{id}")
    public ResponseEntity<MeetingForm> getOneForm(@PathVariable Long id) {
        return meetingFormService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/meetings")
    public ResponseEntity<List<MeetingForm>> getAllForms(Pageable pageRequest) {
        return ResponseEntity.ok(meetingFormService.getAll(pageRequest));
    }

    @PutMapping("/meeting/agreement/{id}/{volunteerId}")
    public ResponseEntity<?> agreeByTourist(@PathVariable(name = "id") Long id,
            @PathVariable(name = "volunteerId") Long volunteerId) {
        return meetingFormService.addVolunteer(id, volunteerId)
                .fold(e -> ResponseEntity.notFound().build(),
                        ResponseEntity::ok);
    }
}