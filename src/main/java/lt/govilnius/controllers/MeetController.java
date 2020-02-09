package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.facadeService.reservation.MeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/meet-management")
public class MeetController {

    @Autowired
    private MeetService meetService;

    @GetMapping("/meets/{id}")
    public ResponseEntity<?> get(@PathVariable("id") long id) {
        final Optional<Meet> meetOptional = meetService.get(id);
        return meetOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.unprocessableEntity().build());
    }

    @GetMapping("/meets")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(meetService.getAll());
    }
}
