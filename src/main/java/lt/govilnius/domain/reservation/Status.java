package lt.govilnius.domain.reservation;

public enum Status {
    NEW,
    SENT_REQUEST,
    SENT_RESPONSE,
    VOLUNTEER_NOT_FOUND,
    VOLUNTEER_NOT_SELECTED,
    AGREED,
    REPORTED,
    FINISHED,
    EVALUATED;
    //NEW, SENT, AGREED, FINISHED, EVALUATED;
}
