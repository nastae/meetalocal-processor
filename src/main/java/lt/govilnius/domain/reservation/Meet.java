package lt.govilnius.domain.reservation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "meet_entity")
public class Meet {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "meet_id")
    private Long id;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "changed_at")
    private Timestamp changedAt;

    @NotNull
    private String email;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    @Column(name = "residence")
    private String residence;

    @NotNull
    @Column(name = "date")
    private Date date;

    @NotNull
    @Column(name = "time")
    private Time time;

    @NotNull
    @Column(name = "people_count")
    private Integer peopleCount;

    @NotNull
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    @Column(name = "age_group")
    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @OneToMany(mappedBy="meet", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<MeetLanguage> languages;

    private String preferences;

    private String comment;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="volunteer", nullable=true)
    private Volunteer volunteer;

    @OneToMany(mappedBy = "meet", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<MeetEngagement> meetEngagements;

    @OneToMany(mappedBy = "meet", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Report> reports;

    public Meet() {}

    public Meet(Timestamp createdAt, Timestamp changedAt, String email,
                String phoneNumber, String name, String surname,
                String residence, Date date, Time time,
                Integer peopleCount, Integer age, Gender gender,
                AgeGroup ageGroup, Set<MeetLanguage> languages, String preferences,
                String comment, Status status, Volunteer volunteer, Set<MeetEngagement> meetEngagements,
                Set<Report> reports) {
        this.createdAt = createdAt;
        this.changedAt = changedAt;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.surname = surname;
        this.residence = residence;
        this.date = date;
        this.time = time;
        this.peopleCount = peopleCount;
        this.age = age;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.languages = languages;
        this.preferences = preferences;
        this.comment = comment;
        this.status = status;
        this.volunteer = volunteer;
        this.meetEngagements = meetEngagements;
        this.reports = reports;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Timestamp changedAt) {
        this.changedAt = changedAt;
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

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public Set<MeetLanguage> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<MeetLanguage> languages) {
        this.languages = languages;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public Set<MeetEngagement> getMeetEngagements() {
        return meetEngagements;
    }

    public void setMeetEngagements(Set<MeetEngagement> meetEngagements) {
        this.meetEngagements = meetEngagements;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }
}
