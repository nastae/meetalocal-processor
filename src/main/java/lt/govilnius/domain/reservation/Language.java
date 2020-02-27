package lt.govilnius.domain.reservation;

import java.util.Arrays;
import java.util.Optional;

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

    public static Optional<Language> fromName(String name) {
        return Arrays.stream(Language.values())
                .filter(a -> a.getName().equals(name))
                .findFirst();
    }
}
