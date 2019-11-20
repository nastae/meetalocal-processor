package lt.govilnius.mail;

import lt.govilnius.models.Person;

public interface EmailService {

    void send(Mail mail, Person volunteer, Person tourist);
}
