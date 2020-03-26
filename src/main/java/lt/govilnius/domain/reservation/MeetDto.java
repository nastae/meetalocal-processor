package lt.govilnius.domain.reservation;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class MeetDto {

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String country;
    private Date date;
    private LocalTime time;
    private Integer peopleCount;
    private Integer age;
    private List<AgeGroup> ageGroups;
    private List<Language> languages;
    private String preferences;
    private String additionalPreferences;

    public MeetDto() {
    }

    public MeetDto(String name, String surname, String email, String phoneNumber,
                   String country, Date date, LocalTime time, Integer peopleCount,
                   Integer age, List<AgeGroup> ageGroups, List<Language> languages,
                   String preferences, String additionalPreferences) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.date = date;
        this.time = time;
        this.peopleCount = peopleCount;
        this.age = age;
        this.ageGroups = ageGroups;
        this.languages = languages;
        this.preferences = preferences;
        this.additionalPreferences = additionalPreferences;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
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
}
