package lt.govilnius.domain.reservation;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@DiscriminatorValue(value = MeetType.Name.LIVE)
public class LiveMeet extends Meet {

    @NotNull
    @Column(name = "phone_number", length = 1000)
    @Lob
    private String phoneNumber;

    @NotNull
    @Column(name = "people_count")
    private Integer peopleCount;

    public LiveMeet() {}

    public LiveMeet(Timestamp createdAt, Timestamp changedAt, @NotNull String name, @NotNull String surname,
                    @NotNull String email, Purpose purpose, @NotNull String country, @NotNull Date date,
                    @NotNull Time time, @NotNull String age, String preferences, String additionalPreferences,
                    Set<MeetAgeGroup> meetAgeGroups, Set<MeetLanguage> languages, Set<MeetEngagement> meetEngagements,
                    Set<MeetStatus> statuses, Volunteer volunteer, Status status, @NotNull Boolean freezed,
                    @NotNull String phoneNumber, @NotNull Integer peopleCount) {
        super(createdAt, changedAt, name, surname, email, purpose, country, date, time, age,
                preferences, additionalPreferences, meetAgeGroups, languages, meetEngagements,
                statuses, volunteer, status, freezed);
        this.phoneNumber = phoneNumber;
        this.peopleCount = peopleCount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }
}
