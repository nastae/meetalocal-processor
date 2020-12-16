package lt.govilnius.domain.reservation;

public enum Status {
    NEW,
    SENT_VOLUNTEER_REQUEST,
    SENT_TOURIST_REQUEST,
    CANCELED,
    AGREED,
    FINISHED,
    ERROR;
}
