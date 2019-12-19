package lt.govilnius.repository.entity;

import lt.govilnius.models.Gender;
import lt.govilnius.models.Status;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "meeting_form_entity")
public class MeetingFormEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "name_and_lastname")
    private String nameAndLastname;

    @Column(name = "from_country")
    private String fromCountry;

    @Column(name = "meeting_date")
    private Date meetingDate;

    @Column(name = "people_count")
    private Integer peopleCount;
    
    private String preferences;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private Integer age;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="volunteer", nullable=true)
    private VolunteerEntity volunteerEntity;

    public MeetingFormEntity() {}

    public MeetingFormEntity(String email, String phoneNumber, String nameAndLastname,
                             String fromCountry, Date meetingDate, Integer peopleCount,
                             String preferences, Status status, Integer age,
                             VolunteerEntity volunteerEntity) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.nameAndLastname = nameAndLastname;
        this.fromCountry = fromCountry;
        this.meetingDate = meetingDate;
        this.peopleCount = peopleCount;
        this.preferences = preferences;
        this.status = status;
        this.age = age;
        this.volunteerEntity = volunteerEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public VolunteerEntity getVolunteerEntity() {
        return volunteerEntity;
    }

    public void setVolunteerEntity(VolunteerEntity volunteerEntity) {
        this.volunteerEntity = volunteerEntity;
    }
}
