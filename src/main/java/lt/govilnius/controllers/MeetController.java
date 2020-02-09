package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.dto.TokenDto;
import lt.govilnius.facadeService.reservation.InteractionWithMeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/meet")
public class MeetController {

    @Autowired
    private InteractionWithMeetService interactionWithMeetService;

    @PostMapping(value = "/agreements",  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> agree(@RequestParam Map<String, String> params) {
        final Optional<MeetEngagement> engagement = interactionWithMeetService.agree(new TokenDto(params.get("token")));
        if (engagement.isPresent())
            return ResponseEntity.ok(engagement.get().getToken());
        else
            return ResponseEntity.unprocessableEntity().build();
    }
}

