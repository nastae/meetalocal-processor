package lt.govilnius.domain.reservation;

import java.util.Arrays;
import java.util.Optional;

public enum MeetType {
    LIVE(Name.LIVE),
    ONLINE(Name.ONLINE);

    private String name;
    MeetType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Optional<MeetType> fromName(String name) {
        return Arrays.stream(MeetType.values())
                .filter(a -> a.getName().equals(name))
                .findFirst();
    }

    public static class Name
    {
        public static final String LIVE = "LIVE";
        public static final String ONLINE = "ONLINE";
    }
}
