package lt.govilnius.mail;

import lt.govilnius.models.Person;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    public void send(Mail mail, Person volunteer, Person tourist) throws MailException {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(mail.getTo());
            message.setSubject(mail.getSubject());
            Map model = new HashMap();
            model.put("volunteerName", volunteer.getName());
            model.put("volunteerLastname", volunteer.getLastName());
            model.put("touristName", tourist.getName());
            String text = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine, "./templates/" + mail.getTemplateName(), model);
            message.setText(text, true);
        };
        this.mailSender.send(preparator);
    }
}