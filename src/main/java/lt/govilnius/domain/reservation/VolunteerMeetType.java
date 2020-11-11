package lt.govilnius.domain.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "volunteer_meet_type_entity")
public class VolunteerMeetType {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "meet_type")
    private MeetType meetType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="volunteer_id", nullable=false)
    private Volunteer volunteer;

    public VolunteerMeetType() {
    }

    public VolunteerMeetType(MeetType meetType, Volunteer volunteer) {
        this.meetType = meetType;
        this.volunteer = volunteer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MeetType getMeetType() {
        return meetType;
    }

    public void setMeetType(MeetType meetType) {
        this.meetType = meetType;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }
}
