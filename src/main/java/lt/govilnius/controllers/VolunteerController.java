package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.facadeService.reservation.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/volunteer-management")
public class VolunteerController {

    @Autowired
    private VolunteerService volunteerService;

    @PostMapping("/volunteers")
    public ResponseEntity<?> add(@RequestBody Volunteer volunteer) {
        final Optional<Volunteer> volunteerOptional = volunteerService.create(volunteer);
        return volunteerOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.unprocessableEntity().build());
    }

    @PutMapping("/volunteers/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @Valid @RequestBody Volunteer volunteer) {
        final Optional<Volunteer> volunteerOptional = volunteerService.edit(id, volunteer);
        return volunteerOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.unprocessableEntity().build());
    }

    @DeleteMapping("/volunteers/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        final boolean deleted = volunteerService.delete(id);
        return deleted ?
                ResponseEntity.ok().build() :
                ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping("/volunteers/{id}")
    public ResponseEntity<?> get(@PathVariable("id") long id) {
        final Optional<Volunteer> volunteerOptional = volunteerService.get(id);
        return volunteerOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.unprocessableEntity().build());
    }

    @GetMapping("/volunteers")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(volunteerService.getAll());
    }
}
