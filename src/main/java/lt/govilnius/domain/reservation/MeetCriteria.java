package lt.govilnius.domain.reservation;

public class MeetCriteria {

    private String meetType;
    private String status;

    public MeetCriteria() {}

    public MeetCriteria(String meetType, String status) {
        this.meetType = meetType;
        this.status = status;
    }

    public String getMeetType() {
        return meetType;
    }

    public void setMeetType(String meetType) {
        this.meetType = meetType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
