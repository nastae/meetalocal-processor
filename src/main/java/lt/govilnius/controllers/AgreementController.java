package lt.govilnius.controllers;

import lt.govilnius.services.MeetingAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.unprocessableEntity;

@RestController
@RequestMapping
public class AgreementController {

    @Autowired
    private MeetingAgreementService agreementService;

    @PutMapping("/agreement/{id}/{isAgreed}")
    public ResponseEntity<?> agreeByVolunteer(@PathVariable(name = "id") Long id,
                                              @PathVariable(name = "isAgreed") Boolean isAgreed) {
        return agreementService.edit(id, isAgreed)
                .fold(e -> unprocessableEntity().body(e), ResponseEntity::ok);
    }
}
