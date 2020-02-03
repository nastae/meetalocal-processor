package lt.govilnius.domain.reservation;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "volunteer_entity")
public class Volunteer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    @Column(nullable = true)
    @OneToMany(mappedBy="volunteer")
    private Set<VolunteerLanguage> languages;

    private String additionalLanguages;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "volunteer")
    private Set<MeetEngagement> meetEngagements;

    public Volunteer() {
    }

    public Volunteer(String name, String surname, Date dateOfBirth,
                     String phoneNumber, String email, Set<VolunteerLanguage> languages,
                     String additionalLanguages, Integer age, Gender gender,
                     String description, Boolean isActive, Set<MeetEngagement> meetEngagements) {
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
        this.isActive = isActive;
        this.meetEngagements = meetEngagements;
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
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Set<MeetEngagement> getMeetEngagements() {
        return meetEngagements;
    }

    public void setMeetEngagements(Set<MeetEngagement> meetEngagements) {
        this.meetEngagements = meetEngagements;
    }
}
