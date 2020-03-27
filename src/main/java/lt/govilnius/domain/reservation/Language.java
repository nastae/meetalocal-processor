package lt.govilnius.domain.reservation;

import java.util.Arrays;
import java.util.Optional;

public enum Language {
    ENGLISH("english"),
    RUSSIAN("russian");

    private String englishName;
    Language(String englishName) {
        this.englishName = englishName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public static Optional<Language> fromEnglishName(String name) {
        return Arrays.stream(Language.values())
                .filter(a -> a.getEnglishName().equals(name))
                .findFirst();
    }
}
