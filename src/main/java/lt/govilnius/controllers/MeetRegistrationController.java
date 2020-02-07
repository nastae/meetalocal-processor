package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.facadeService.reservation.MeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.unprocessableEntity;

@RestController
@RequestMapping("/api")
public class MeetRegistrationController {

    @Autowired
    private MeetService meetService;

    @PostMapping("/meets")
    public ResponseEntity<?> newMeet(@Valid @RequestBody Meet meet) {
        System.out.println(meet.getEmail());
        Optional<Meet> meetOptional = meetService.create(meet);
        if (meetOptional.isPresent())
            return ResponseEntity.ok(meetOptional.get());
        else
            return unprocessableEntity().build();
    }
}
