package lt.govilnius.domainService.mail;

public class MailSendingException extends Exception {

    public MailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}