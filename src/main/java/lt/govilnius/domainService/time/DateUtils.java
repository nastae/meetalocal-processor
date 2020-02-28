package lt.govilnius.domainService.time;

import java.sql.Date;

public class DateUtils {

    public static Long yearsFromNow(Date sqlDate) {
        Long val = ((long) (System.currentTimeMillis() - (sqlDate.getTime() - 31556952000L * 1900)) / 31556952000L) - 1900;
        return  val;
    }
}
