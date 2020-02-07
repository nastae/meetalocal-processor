package lt.govilnius.domainService.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MailSendingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSendingProcessor.class);

    @Autowired
    private MailSendingService mailSendingService;

    @Scheduled(fixedDelay = 5000L)
    public void processNew() {
        LOGGER.info("Starts process new meet");
        mailSendingService.processNews();
        LOGGER.info("Ends process new meets");
    }

    @Scheduled(fixedDelay = 5000L)
    public void processRequests() {
        LOGGER.info("Starts process sent requested meet to volunteers");
        mailSendingService.processRequests();
        LOGGER.info("Ends process sent request to volunteers");
    }

    @Scheduled(fixedDelay = 5000L)
    public void processAdditionals() {
        LOGGER.info("Starts process meets that volunteers haven't found");
        mailSendingService.processAdditionals();
        LOGGER.info("Ends process meets that volunteers haven't found");
    }

    @Scheduled(fixedDelay = 5000L)
    public void processResponses() {
        LOGGER.info("Starts process responded meets by tourists");
        mailSendingService.processResponses();
        LOGGER.info("Ends process responded meets by tourists");
    }

    @Scheduled(fixedDelay = 5000L)
    public void processAgreements() {
        LOGGER.info("Starts process agreed meets by tourists");
        mailSendingService.processAgreements();
        LOGGER.info("Ends process agreed meets by tourists");
    }
}
