package lt.govilnius.domain.reservation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Time;

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

    @NotNull
    @Column(name = "time")
    private Time time;

    @NotNull
    @Column(name = "token", unique = true)
    private String token;

    @NotNull
    @Column(name = "engaged")
    private Boolean engaged;

    public MeetEngagement() {
    }

    public MeetEngagement(Meet meet, Volunteer volunteer, Time time, String token, Boolean engaged) {
        this.meet = meet;
        this.volunteer = volunteer;
        this.time = time;
        this.token = token;
        this.engaged = engaged;
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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getEngaged() {
        return engaged;
    }

    public void setEngaged(Boolean engaged) {
        this.engaged = engaged;
    }
}
