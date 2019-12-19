package lt.govilnius.repository.entity;

import javax.persistence.*;

@Entity
@Table(name = "meeting_agreement_entity")
public class MeetingAgreementEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="meeting_form", nullable=false)
    private MeetingFormEntity meetingForm;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="volunteer", nullable = false)
    private VolunteerEntity volunteerEntity;

    @Column(name = "is_agreed")
    private Boolean isAgreed;

    public MeetingAgreementEntity() {}

    public MeetingAgreementEntity(MeetingFormEntity meetingForm,
                                  VolunteerEntity volunteerEntity, Boolean isAgreed) {
        this.meetingForm = meetingForm;
        this.volunteerEntity = volunteerEntity;
        this.isAgreed = isAgreed;
    }

    public MeetingFormEntity getMeetingForm() {
        return meetingForm;
    }

    public void setMeetingForm(MeetingFormEntity meetingForm) {
        this.meetingForm = meetingForm;
    }

    public VolunteerEntity getVolunteerEntity() {
        return volunteerEntity;
    }

    public void setVolunteerEntity(VolunteerEntity volunteerEntity) {
        this.volunteerEntity = volunteerEntity;
    }

    public Boolean getAgreed() {
        return isAgreed;
    }

    public void setAgreed(Boolean agreed) {
        isAgreed = agreed;
    }
}
