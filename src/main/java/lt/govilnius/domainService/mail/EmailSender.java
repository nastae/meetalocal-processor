package lt.govilnius.domainService.mail;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Component
public class EmailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

    @Autowired
    public JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    public synchronized void send(Mail mail, EmailSenderConfig config) {
        try {
            sendTo(mail.getTo(), config);
        } catch (RuntimeException ex) {
            LOGGER.error("Fail to send message to " + mail.getTo() + "! ", ex);
        }
    }

    private void sendTo(String to, EmailSenderConfig config) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(to);
            message.setSubject(config.getSubject());
            String text = VelocityEngineUtils.mergeTemplateIntoString(
                    velocityEngine, config.getPath(), "Windows-1257", config.getModel());
            message.setText(text, true);
        };
        this.mailSender.send(preparator);
    }
}
