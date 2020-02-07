package lt.govilnius.domain.reservation;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "volunteer_entity")
public class Volunteer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "volunteer_id")
    private Long id;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "changed_at")
    private Timestamp changedAt;

    private String name;
    private String surname;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    @Column(nullable = true)
    @OneToMany(mappedBy="volunteer", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<VolunteerLanguage> languages;

    private String additionalLanguages;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String description;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "volunteer", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<MeetEngagement> meetEngagements;

    @OneToMany(mappedBy = "volunteer", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Report> reports;

    public Volunteer() {
    }

    public Volunteer(Timestamp createdAt, Timestamp changedAt, String name, String surname, Date dateOfBirth,
                     String phoneNumber, String email, Set<VolunteerLanguage> languages,
                     String additionalLanguages, Integer age, Gender gender,
                     String description, Boolean active, Set<MeetEngagement> meetEngagements,
                     Set<Report> reports) {
        this.createdAt = createdAt;
        this.changedAt = changedAt;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.languages = languages;
        this.additionalLanguages = additionalLanguages;
        this.age = age;
        this.gender = gender;
        this.description = description;
        this.active = active;
        this.meetEngagements = meetEngagements;
        this.reports = reports;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<VolunteerLanguage> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<VolunteerLanguage> languages) {
        this.languages = languages;
    }

    public String getAdditionalLanguages() {
        return additionalLanguages;
    }

    public void setAdditionalLanguages(String additionalLanguages) {
        this.additionalLanguages = additionalLanguages;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<MeetEngagement> getMeetEngagements() {
        return meetEngagements;
    }

    public void setMeetEngagements(Set<MeetEngagement> meetEngagements) {
        this.meetEngagements = meetEngagements;
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

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }
}
