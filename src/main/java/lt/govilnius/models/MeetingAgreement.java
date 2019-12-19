package lt.govilnius.models;

public class MeetingAgreement {

    private Long id;
    private Long meetingFormId;
    private Long volunteerId;
    private Boolean isAgreed;

    public MeetingAgreement() {}

    public MeetingAgreement(Long meetingFormId, Long volunteerId, Boolean isAgreed) {
        this.meetingFormId = meetingFormId;
        this.volunteerId = volunteerId;
        this.isAgreed = isAgreed;
    }

    public Long getMeetingFormId() {
        return meetingFormId;
    }

    public void setMeetingFormId(Long meetingFormId) {
        this.meetingFormId = meetingFormId;
    }

    public Long getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(Long volunteerId) {
        this.volunteerId = volunteerId;
    }

    public Boolean getAgreed() {
        return isAgreed;
    }

    public void setAgreed(Boolean agreed) {
        isAgreed = agreed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
