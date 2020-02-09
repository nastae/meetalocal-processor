package lt.govilnius.domain.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "volunteer_language_entity")
public class VolunteerLanguage implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Language language;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="volunteer_id", nullable=false)
    private Volunteer volunteer;

    public VolunteerLanguage() {
    }

    public VolunteerLanguage(Language language, Volunteer volunteer) {
        this.language = language;
        this.volunteer = volunteer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }
}
