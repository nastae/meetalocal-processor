package lt.govilnius.domain.reservation;

import java.util.Arrays;
import java.util.Optional;

public enum AgeGroup {
    YOUTH(18, 29),
    JUNIOR_ADULTS(30, 39),
    SENIOR_ADULTS(40, 49),
    SENIORS(50, 122);

    private Integer from;
    private Integer to;
    AgeGroup(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public static Optional<AgeGroup> fromString(String name) {
        return Arrays.stream(AgeGroup.values())
                .filter(a -> a.toString().equals(name))
                .findAny();
    }
}
