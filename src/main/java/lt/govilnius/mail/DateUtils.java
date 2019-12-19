package lt.govilnius.mail;

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
                return "balandžio";
            case 4:
                return "gegužio";
            case 5:
                return "birželio";
            case 6:
                return "liepos";
            case 7:
                return "rugpjūčio";
            case 8:
                return "rugsėjo";
            case 9:
                return "spalio";
            case 10:
                return "lapkričio";
            case 11:
                return "gruodžio";
            default:
                throw new RuntimeException("Month number is incorrect!");
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
                throw new RuntimeException("Month number is incorrect!");
        }
    }
}
