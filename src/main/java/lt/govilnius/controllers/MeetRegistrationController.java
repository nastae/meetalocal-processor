package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetDto;
import lt.govilnius.facadeService.reservation.MeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.unprocessableEntity;

@RestController
@RequestMapping("/registration")
public class MeetRegistrationController {

    @Autowired
    private MeetService meetService;

    @PostMapping("/meets")
    public ResponseEntity<?> newMeet(@Valid @RequestBody MeetDto meet) {
        final Optional<Meet> meetOptional = meetService.create(meet);
        if (meetOptional.isPresent())
            return ResponseEntity.ok(meetOptional.get());
        else
            return unprocessableEntity().build();
    }
}
