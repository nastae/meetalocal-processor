package lt.govilnius.domain.reservation;

public enum Gender {
    MALE("Male"), FEMALE("Female"), OTHER("Other");

    private String name;
    Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
