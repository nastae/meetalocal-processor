package lt.govilnius.domainService.mail;

import java.util.Map;

public class EmailSenderConfig {

    private String subject;
    private String path;
    private Map<String, Object> model;

    public EmailSenderConfig(String path, Map<String, Object> model, String subject) {
        this.path = path;
        this.model = model;
        this.subject = subject;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public String getSubject() {
        return subject;
    }
}