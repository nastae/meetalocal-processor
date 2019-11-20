package lt.govilnius.processor;

import lt.govilnius.mail.EmailService;
import lt.govilnius.mail.Mail;
import lt.govilnius.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MailSending {

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 50000L)
    public void sendMails() {
        emailService.send(new Mail("aurisgo1998@gmail.com", "Meet a local",
                        "volunteer_invitation.vm"),
                new Person("Aurimas", "Golotylecas"),
                new Person("Birute", "Birzute"));
    }
}
