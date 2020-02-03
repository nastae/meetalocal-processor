package lt.govilnius.domain.reservation;

import javax.persistence.*;

@Entity
@Table(name = "meet_engagement_entity")
public class MeetEngagement {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meet_id")
    private Meet meet;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;

    public MeetEngagement() {
    }

    public MeetEngagement(Meet meet, Volunteer volunteer) {
        this.meet = meet;
        this.volunteer = volunteer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Meet getMeet() {
        return meet;
    }

    public void setMeet(Meet meet) {
        this.meet = meet;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }
}
