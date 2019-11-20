package lt.govilnius.mail;

public class Mail {

    private String to;
    private String subject;
    private String templateName;

    public Mail(String to, String subject, String templateName) {
        this.to = to;
        this.subject = subject;
        this.templateName = templateName;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
