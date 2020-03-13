package lt.govilnius.domain.reservation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

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
    @Column(name = "confirmed")
    private Boolean confirmed;

    @OneToMany(mappedBy="engagement", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Evaluation> evaluations;

    @NotNull
    private Boolean freezed;

    public MeetEngagement() {
    }

    public MeetEngagement(Meet meet, Volunteer volunteer, Time time, String token, Boolean confirmed, Boolean freezed) {
        this.meet = meet;
        this.volunteer = volunteer;
        this.time = time;
        this.token = token;
        this.confirmed = confirmed;
        this.freezed = freezed;
        this.evaluations = new HashSet<>();
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

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Set<Evaluation> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(Set<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }

    public Boolean getFreezed() {
        return freezed;
    }

    public void setFreezed(Boolean freezed) {
        this.freezed = freezed;
    }
}
