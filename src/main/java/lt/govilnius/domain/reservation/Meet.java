package lt.govilnius.domain.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String email;

    @NotNull
    @Column(name = "phone_number", length = 1000)
    @Lob
    private String phoneNumber;

    @NotNull
    @Column(name = "country")
    private String country;

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
    private String age;

    @OneToMany(mappedBy="meet", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<MeetAgeGroup> meetAgeGroups;

    @OneToMany(mappedBy="meet", fetch=FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<MeetLanguage> languages;

    private String preferences;

    @Lob
    @Column(name = "additional_preferences", length = 1000)
    private String additionalPreferences;

    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private Boolean freezed;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="volunteer", nullable=true)
    private Volunteer volunteer;

    @JsonIgnore
    @OneToMany(mappedBy = "meet", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<MeetEngagement> meetEngagements;

    @OneToMany(mappedBy = "meet", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<MeetStatus> statuses;

    public Meet() {}

    public Meet(Timestamp createdAt, Timestamp changedAt,
                @NotNull String name, @NotNull String surname, @NotNull String email,
                @NotNull String phoneNumber, @NotNull String country, @NotNull Date date,
                @NotNull Time time, @NotNull Integer peopleCount, @NotNull String age,
                Set<MeetAgeGroup> meetAgeGroups, Set<MeetLanguage> languages, String preferences, String additionalPreferences,
                Status status, Boolean freezed, Volunteer volunteer, Set<MeetEngagement> meetEngagements,
                Set<MeetStatus> statuses) {
        this.createdAt = createdAt;
        this.changedAt = changedAt;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.date = date;
        this.time = time;
        this.peopleCount = peopleCount;
        this.age = age;
        this.meetAgeGroups = meetAgeGroups;
        this.languages = languages;
        this.preferences = preferences;
        this.additionalPreferences = additionalPreferences;
        this.status = status;
        this.volunteer = volunteer;
        this.meetEngagements = meetEngagements;
        this.statuses = statuses;
        this.freezed = freezed;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Set<MeetAgeGroup> getMeetAgeGroups() {
        return meetAgeGroups;
    }

    public void setMeetAgeGroups(Set<MeetAgeGroup> meetAgeGroups) {
        this.meetAgeGroups = meetAgeGroups;
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

    public Set<MeetStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(Set<MeetStatus> statuses) {
        this.statuses = statuses;
    }

    public Boolean getFreezed() {
        return freezed;
    }

    public void setFreezed(Boolean freezed) {
        this.freezed = freezed;
    }

    public String getAdditionalPreferences() {
        return additionalPreferences;
    }

    public void setAdditionalPreferences(String additionalPreferences) {
        this.additionalPreferences = additionalPreferences;
    }
}
