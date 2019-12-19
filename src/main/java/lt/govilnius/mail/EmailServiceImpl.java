package lt.govilnius.mail;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.Map;

@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    public void send(Mail mail, Map model, String templateLocation) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(mail.getTo());
            message.setSubject(mail.getSubject());
            String text = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine, templateLocation, model);
            message.setText(text, true);
        };
        this.mailSender.send(preparator);
    }
}