package lt.govilnius.models;

import java.util.Date;

public class MeetingForm {

    private Integer id;
    private String email;
    private String phoneNumber;
    private String nameAndLastname;
    private String fromCountry;
    private Date meetingDate;
    private Integer peopleCount;
    private String preferences;
    private Status status;
    private String gender;
    private Integer age;

    public MeetingForm() {}

    public MeetingForm(Integer id, String email, String phoneNumber, String nameAndLastname,
                       String fromCountry, Date meetingDate, Integer peopleCount,
                       String preferences, Status status, String gender, Integer age) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nameAndLastname = nameAndLastname;
        this.fromCountry = fromCountry;
        this.meetingDate = meetingDate;
        this.peopleCount = peopleCount;
        this.preferences = preferences;
        this.status = status;
        this.gender = gender;
        this.age = age;
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

    public String getNameAndLastname() {
        return nameAndLastname;
    }

    public void setNameAndLastname(String nameAndLastname) {
        this.nameAndLastname = nameAndLastname;
    }

    public String getFromCountry() {
        return fromCountry;
    }

    public void setFromCountry(String fromCountry) {
        this.fromCountry = fromCountry;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
