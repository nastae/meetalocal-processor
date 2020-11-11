package lt.govilnius.domain.reservation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@DiscriminatorValue(value = MeetType.Name.ONLINE)
public class OnlineMeet extends Meet {

    @NotNull
    @Column(name = "skype_name")
    private String skypeName;

    public OnlineMeet() {}

    public OnlineMeet(Timestamp createdAt, Timestamp changedAt, @NotNull String name, @NotNull String surname,
                      @NotNull String email, Purpose purpose, @NotNull String country, @NotNull Date date,
                      @NotNull Time time, @NotNull String age, String preferences, String additionalPreferences,
                      Set<MeetAgeGroup> meetAgeGroups, Set<MeetLanguage> languages, Set<MeetEngagement> meetEngagements,
                      Set<MeetStatus> statuses, Volunteer volunteer, Status status, @NotNull Boolean freezed,
                      @NotNull String skypeName) {
        super(createdAt, changedAt, name, surname, email, purpose, country, date, time, age, preferences,
                additionalPreferences, meetAgeGroups, languages, meetEngagements, statuses, volunteer, status, freezed);
        this.skypeName = skypeName;
    }

    public String getSkypeName() {
        return skypeName;
    }

    public void setSkypeName(String skypeName) {
        this.skypeName = skypeName;
    }
}
