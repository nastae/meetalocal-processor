package lt.govilnius.domain.reservation;

public class CheckedLanguage {
    private Language language;
    private Boolean checked;

    public CheckedLanguage(Language language, Boolean checked) {
        this.language = language;
        this.checked = checked;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}