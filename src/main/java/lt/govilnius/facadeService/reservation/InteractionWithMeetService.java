package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.domain.reservation.dto.TokenDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InteractionWithMeetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionWithMeetService.class);

    @Autowired
    private MeetEngagementService meetEngagementService;

    @Value("${waiting.sent.request.milliseconds}")
    private Long sentRequestWaiting;

    public Optional<MeetEngagement> agree(TokenDto token) {
        LOGGER.info("Process agreement of the meet engagement with token " + token.getToken());
        final Optional<MeetEngagement> engagement = meetEngagementService.getByToken(token.getToken());
        return engagement
                .filter(e -> e.getMeet().getStatus().equals(Status.SENT_VOLUNTEER_REQUEST))
                .filter(e -> System.currentTimeMillis() - e.getMeet().getChangedAt().getTime() < sentRequestWaiting)
                .map(e -> {
                    LOGGER.info("Edit the meet engagement with token " + token.getToken());
                    e.setEngaged(true);
                    return meetEngagementService.edit(e.getId(), e).orElse(null);
                });
    }
}
