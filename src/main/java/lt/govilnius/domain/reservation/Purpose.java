package lt.govilnius.domain.reservation;

import java.util.Arrays;
import java.util.Optional;

public enum Purpose {
    TOURISM("Tourism"),
    RELOCATION("Relocation");

    private String name;
    Purpose(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Optional<Purpose> fromName(String name) {
        return Arrays.stream(Purpose.values())
                .filter(a -> a.getName().equals(name))
                .findFirst();
    }
}
