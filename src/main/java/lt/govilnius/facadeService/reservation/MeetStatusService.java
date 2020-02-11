package lt.govilnius.facadeService.reservation;

import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetStatus;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.repository.reservation.MeetStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class MeetStatusService {

    @Autowired
    private MeetStatusRepository meetStatusRepository;

    public Optional<MeetStatus> create(Meet meet, Status status) {
        return Optional.of(add(meet, status));
    }

    private MeetStatus add(Meet meet, Status status) {
        final MeetStatus entity = new MeetStatus();
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entity.setMeet(meet);
        entity.setStatus(status);
        return meetStatusRepository.save(entity);
    }

    public List<MeetStatus> getByMeetId(Long meetId) {
        return meetStatusRepository.findByMeetId(meetId);
    }
}
