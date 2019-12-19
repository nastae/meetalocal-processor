package lt.govilnius.mail;

import java.util.Map;

public interface EmailService {

    void send(Mail mail, Map model, String templateLocation);
}
