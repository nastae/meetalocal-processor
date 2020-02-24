package lt.govilnius.domain.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "meet_age_group_entity")
public class MeetAgeGroup {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="meet_id", nullable=false)
    private Meet meet;

    public MeetAgeGroup() {
    }

    public MeetAgeGroup(AgeGroup ageGroup, Meet meet) {
        this.ageGroup = ageGroup;
        this.meet = meet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public Meet getMeet() {
        return meet;
    }

    public void setMeet(Meet meet) {
        this.meet = meet;
    }
}
