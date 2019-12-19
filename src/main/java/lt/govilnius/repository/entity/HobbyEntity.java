package lt.govilnius.repository.entity;

import lt.govilnius.models.Hobby;

import javax.persistence.*;

@Entity
@Table(name = "hobby_entity")
public class HobbyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Hobby hobby;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="meeting_form", nullable=false)
    private MeetingFormEntity meetingForm;

    public HobbyEntity() {}

    public HobbyEntity(Hobby hobby, MeetingFormEntity meetingForm) {
        this.hobby = hobby;
        this.meetingForm = meetingForm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hobby getHobby() {
        return hobby;
    }

    public void setHobby(Hobby hobby) {
        this.hobby = hobby;
    }

    public MeetingFormEntity getMeetingForm() {
        return meetingForm;
    }

    public void setMeetingForm(MeetingFormEntity meetingForm) {
        this.meetingForm = meetingForm;
    }
}
