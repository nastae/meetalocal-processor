package lt.govilnius.controllers;

import lt.govilnius.domain.reservation.AgeGroup;
import lt.govilnius.domain.reservation.Gender;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.facadeService.reservation.MeetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashSet;
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

    @GetMapping("/meets")
    public Meet meet() {
        Meet meet = sampleMeet();
        meetService.create(meet);
        return meetService.getAll().get(0);
    }

    public static Meet sampleMeet() {
        return new Meet(
                new Timestamp(2019, 1, 1, 1, 1, 1, 1),
                new Timestamp(2019, 1, 1, 1, 1, 1, 2),
                "meetalocaltest@gmail.com", "123000", "Test", "Test",
                "Vilnius", new Date(2019, 2, 2), new Time(12, 12, 26),
                1, 26, Gender.MALE,
                AgeGroup.YOUTH, new HashSet<>(), "none",
                "comments", Status.NEW, null, new HashSet<>(), new HashSet<>());
    }

}
