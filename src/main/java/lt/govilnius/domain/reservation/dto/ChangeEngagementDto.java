package lt.govilnius.domain.reservation.dto;

import java.sql.Time;

public class ChangeEngagementDto extends TokenDto {

    private Time time;

    public ChangeEngagementDto(String token, Time time) {
        super(token);
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
