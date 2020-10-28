package lt.govilnius.domain.reservation;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class MeetDto {

    private String name;
    private String surname;
    private Purpose purpose;
    private String email;
    private String skypeName;
    private String country;
    private Date date;
    private LocalTime time;
    private String age;
    private List<AgeGroup> ageGroups;
    private List<Language> languages;
    private String preferences;
    private String additionalPreferences;

    public MeetDto() {
    }

    public MeetDto(String name, String surname, Purpose purpose, String email, String skypeName,
                   String country, Date date, LocalTime time, Integer peopleCount,
                   String age, List<AgeGroup> ageGroups, List<Language> languages,
                   String preferences, String additionalPreferences) {
        this.name = name;
        this.surname = surname;
        this.purpose = purpose;
        this.email = email;
        this.skypeName = skypeName;
        this.country = country;
        this.date = date;
        this.time = time;
        this.age = age;
        this.ageGroups = ageGroups;
        this.languages = languages;
        this.preferences = preferences;
        this.additionalPreferences = additionalPreferences;
    }

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public List<AgeGroup> getAgeGroups() {
        return ageGroups;
    }

    public void setAgeGroups(List<AgeGroup> ageGroups) {
        this.ageGroups = ageGroups;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getAdditionalPreferences() {
        return additionalPreferences;
    }

    public void setAdditionalPreferences(String additionalPreferences) {
        this.additionalPreferences = additionalPreferences;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getSkypeName() {
        return skypeName;
    }

    public void setSkypeName(String skypeName) {
        this.skypeName = skypeName;
    }
}
