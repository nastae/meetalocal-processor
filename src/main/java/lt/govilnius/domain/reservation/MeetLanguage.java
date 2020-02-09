package lt.govilnius.domain.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "meet_language_entity")
public class MeetLanguage implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Language language;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="meet_id", nullable=false)
    private Meet meet;

    public MeetLanguage() {
    }

    public MeetLanguage(Language language, Meet meet) {
        this.language = language;
        this.meet = meet;
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

    public Meet getMeet() {
        return meet;
    }

    public void setMeet(Meet meet) {
        this.meet = meet;
    }
}
