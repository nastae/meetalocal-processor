package lt.govilnius.domain.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "volunteer_purpose_entity")
public class VolunteerPurpose {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Purpose purpose;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="volunteer_id", nullable=false)
    private Volunteer volunteer;

    public VolunteerPurpose() {
    }

    public VolunteerPurpose(Purpose purpose, Volunteer volunteer) {
        this.purpose = purpose;
        this.volunteer = volunteer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }
}
