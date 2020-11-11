package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.LiveMeet;
import lt.govilnius.domain.reservation.LiveMeetDto;
import lt.govilnius.facadeService.reservation.LiveMeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.unprocessableEntity;

@RestController
@RequestMapping("/live-meet")
public class LiveMeetController {

    @Autowired
    private LiveMeetService liveMeetService;

    @PostMapping("/meets")
    public ResponseEntity<?> newMeet(@Valid @RequestBody LiveMeetDto meet) {
        final Optional<LiveMeet> meetOptional = liveMeetService.create(meet);
        if (meetOptional.isPresent())
            return ResponseEntity.ok(meetOptional.get());
        else
            return unprocessableEntity().build();
    }
}
