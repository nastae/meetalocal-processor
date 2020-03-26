package lt.govilnius.domainService.time;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateUtils {

    public static Long yearsFromNow(Date sqlDate) {
        return ((long) (System.currentTimeMillis() - (sqlDate.getTime() - 31556952000L * 1900)) / 31556952000L) - 1900;
    }
}
