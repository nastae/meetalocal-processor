package lt.govilnius.domain.reservation;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class LiveMeetDto extends MeetDto {

    private String phoneNumber;

    private Integer peopleCount;

    public LiveMeetDto() {}

    public LiveMeetDto(String name, String surname, Purpose purpose, String email, String phoneNumber,
                       String country, Date date, LocalTime time, Integer peopleCount, String age,
                       List<AgeGroup> ageGroups, List<Language> languages, String preferences,
                       String additionalPreferences) {
        super(name, surname, purpose, email, country, date, time, age,
                ageGroups, languages, preferences, additionalPreferences);
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
