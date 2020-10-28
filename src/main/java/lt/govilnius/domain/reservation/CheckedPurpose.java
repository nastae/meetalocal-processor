package lt.govilnius.domain.reservation;

public class CheckedPurpose {
    private Purpose purpose;
    private Boolean checked;

    public CheckedPurpose(Purpose purpose, Boolean checked) {
        this.purpose = purpose;
        this.checked = checked;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
