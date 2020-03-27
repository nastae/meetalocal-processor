package lt.govilnius.domain.reservation;

import java.util.Arrays;
import java.util.Optional;

public enum Language {
    ENGLISH("english", "anglų"),
    RUSSIAN("russian", "rusų");

    private String englishName;
    private String lithuanianName;
    Language(String englishName, String lithuanianName) {
        this.englishName = englishName;
        this.lithuanianName = lithuanianName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getLithuanianName() {
        return lithuanianName;
    }

    public static Optional<Language> fromEnglishName(String name) {
        return Arrays.stream(Language.values())
                .filter(a -> a.getEnglishName().equals(name))
                .findFirst();
    }
}
