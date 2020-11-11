package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.OnlineMeet;
import lt.govilnius.domain.reservation.OnlineMeetDto;
import lt.govilnius.facadeService.reservation.OnlineMeetService;
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
@RequestMapping("/online-meet")
public class OnlineMeetController {

    @Autowired
    private OnlineMeetService onlineMeetService;

    @PostMapping("/meets")
    public ResponseEntity<?> newMeet(@Valid @RequestBody OnlineMeetDto meet) {
        final Optional<OnlineMeet> meetOptional = onlineMeetService.create(meet);
        if (meetOptional.isPresent())
            return ResponseEntity.ok(meetOptional.get());
        else
            return unprocessableEntity().build();
    }
}
