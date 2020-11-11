package lt.govilnius.domain.reservation;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class OnlineMeetDto extends MeetDto {

    private String skypeName;

    public OnlineMeetDto() {}

    public OnlineMeetDto(String name, String surname, Purpose purpose, String email, String country, Date date,
                         LocalTime time, String age, List<AgeGroup> ageGroups, List<Language> languages,
                         String preferences, String additionalPreferences, String skypeName) {
        super(name, surname, purpose, email, country, date, time, age, ageGroups, languages,
                preferences, additionalPreferences);
        this.skypeName = skypeName;
    }

    public String getSkypeName() {
        return skypeName;
    }

    public void setSkypeName(String skypeName) {
        this.skypeName = skypeName;
    }
}
