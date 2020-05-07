package lt.govilnius.domainService.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MailProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailProcessor.class);

    @Autowired
    private VolunteerMailProcessor volunteerMailProcessor;

    @Autowired
    private TouristMailProcessor touristMailProcessor;

    @Scheduled(fixedDelay = 60000L)
    public void processNew() {
        LOGGER.info("Starts process new meet");
        volunteerMailProcessor.processNews();
        LOGGER.info("Ends process new meets");
    }

    @Scheduled(fixedDelay = 60000L)
    public void processTouristRequests() {
        LOGGER.info("Starts process volunteer sent requested meets");
        volunteerMailProcessor.processRequests();
        LOGGER.info("Ends process volunteer sent requested meets");
    }

    @Scheduled(fixedDelay = 60000L)
    public void processAgreements() {
        LOGGER.info("Starts process agreed meets by tourists");
        volunteerMailProcessor.processAgreements();
        LOGGER.info("Ends process agreed meets by tourists");
    }

    @Scheduled(fixedDelay = 60000L)
    public void processVolunteerRequests() {
        LOGGER.info("Starts process tourist sent requested meets");
        touristMailProcessor.processRequests();
        LOGGER.info("Ends process tourist sent requested meets");
    }
}
