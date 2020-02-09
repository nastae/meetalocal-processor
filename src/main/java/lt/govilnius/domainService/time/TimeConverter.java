package lt.govilnius.domainService.time;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeConverter {

    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm:ss");

    public static Time convertToTime(String timeString) throws ParseException {
        return new Time(TIME_FORMAT.parse(timeString).getTime());
    }
}
