package lt.govilnius.domainService.mail;

public class DateUtils {

    public static String monthToLithuanian(int month) throws RuntimeException {
        switch (month) {
            case 0:
                return "sausio";
            case 1:
                return "vasario";
            case 2:
                return "kovo";
            case 3:
                return "balandzio";
            case 4:
                return "geguzes";
            case 5:
                return "birzelio";
            case 6:
                return "liepos";
            case 7:
                return "rugpjucio";
            case 8:
                return "rugsejo";
            case 9:
                return "spalio";
            case 10:
                return "lapkricio";
            case 11:
                return "gruodzio";
            default:
                throw new RuntimeException("Month doesn't exist with index " + month);
        }
    }

    public static String monthToEnglish(int month) throws RuntimeException {
        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "Maarch";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                throw new RuntimeException("Month doesn't exist with index " + month);
        }
    }
}
