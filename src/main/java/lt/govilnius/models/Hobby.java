package lt.govilnius.models;

public enum Hobby {
    ART("Art");

    private String name;
    Hobby(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
