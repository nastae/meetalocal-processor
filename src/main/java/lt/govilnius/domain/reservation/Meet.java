package lt.govilnius.domain.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.EnumSet;
import java.util.Set;

@Entity(name = "meet_entity")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type",
        discriminatorType = DiscriminatorType.STRING)
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

    @Enumerated(EnumType.STRING)
    private Purpose purpose;

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
    @Column(name = "age", nullable = false)
    private String age;

    private String preferences;

    @Lob
    @Column(name = "additional_preferences", length = 1000)
    private String additionalPreferences;

    @OneToMany(mappedBy="meet", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<MeetAgeGroup> meetAgeGroups;

    @OneToMany(mappedBy="meet", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<MeetLanguage> languages;

    @JsonIgnore
    @OneToMany(mappedBy = "meet", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<MeetEngagement> meetEngagements;

    @OneToMany(mappedBy = "meet", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<MeetStatus> statuses;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="volunteer", nullable=true)
    private Volunteer volunteer;

    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private Boolean freezed;

    @Column(name="type", insertable = false, updatable = false)
    private String type;

    public Meet() {}

    // TODO: Ar reikia prideti type?
    public Meet(Timestamp createdAt, Timestamp changedAt, @NotNull String name, @NotNull String surname,
                @NotNull String email, Purpose purpose, @NotNull String country, @NotNull Date date,
                @NotNull Time time, @NotNull String age, String preferences, String additionalPreferences,
                Set<MeetAgeGroup> meetAgeGroups, Set<MeetLanguage> languages, Set<MeetEngagement> meetEngagements,
                Set<MeetStatus> statuses, Volunteer volunteer, Status status, @NotNull Boolean freezed) {
        this.createdAt = createdAt;
        this.changedAt = changedAt;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.purpose = purpose;
        this.country = country;
        this.date = date;
        this.time = time;
        this.age = age;
        this.preferences = preferences;
        this.additionalPreferences = additionalPreferences;
        this.meetAgeGroups = meetAgeGroups;
        this.languages = languages;
        this.meetEngagements = meetEngagements;
        this.statuses = statuses;
        this.volunteer = volunteer;
        this.status = status;
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

    public Purpose getPurpose() {
        return purpose;
    }

    public void setPurpose(Purpose purpose) {
        this.purpose = purpose;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getFreezed() {
        return freezed;
    }

    public void setFreezed(Boolean freezed) {
        this.freezed = freezed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
