package lt.govilnius.domainService.mail;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Component
public class EmailSender {

    @Autowired
    public JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    public void send(Mail mail, EmailSenderConfig config) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(mail.getTo());
            message.setSubject(config.getSubject());
            String text = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine, config.getTemplate().getPath(), config.getModel());
            message.setText(text, true);
        };
        this.mailSender.send(preparator);
    }
}