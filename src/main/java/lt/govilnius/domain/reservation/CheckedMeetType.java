package lt.govilnius.domain.reservation;

public class CheckedMeetType {

    private MeetType meetType;
    private Boolean checked;

    public CheckedMeetType(MeetType meetType, Boolean checked) {
        this.meetType = meetType;
        this.checked = checked;
    }

    public MeetType getMeetType() {
        return meetType;
    }

    public void setMeetType(MeetType meetType) {
        this.meetType = meetType;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
