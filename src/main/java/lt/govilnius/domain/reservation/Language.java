package lt.govilnius.domain.reservation;

public enum Language {
    ENGLISH("English"),
    RUSSIAN("Russian");

    private String name;
    Language(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
