package lt.govilnius.domain.reservation;

public enum Status {
    NEW,
    SENT_VOLUNTEER_REQUEST,
    SENT_TOURIST_REQUEST,
    SENT_TOURIST_ADDITION,
    CANCELLATION,
    AGREED,
    REPORTED,
    FINISHED,
    SENT_VOLUNTEER_REQUEST_AFTER_ADDITION,
    ERROR;
}
