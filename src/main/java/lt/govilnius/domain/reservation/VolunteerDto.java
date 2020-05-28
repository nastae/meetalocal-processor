package lt.govilnius.domain.reservation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolunteerDto {

    private Long id;

    private String name;

    private String surname;

    private Date dateOfBirth;

    private String skypeName;

    private String email;

    private List<String> languages = new ArrayList<>();

    private String description;

    private Boolean active = true;

    public VolunteerDto() {}

    public VolunteerDto(Volunteer volunteer) {
        this(volunteer.getId(), volunteer.getName(), volunteer.getSurname(), volunteer.getDateOfBirth(), volunteer.getSkypeName(),
                volunteer.getEmail(), volunteer.getLanguages().stream().map(l -> l.getLanguage().getName()).collect(Collectors.toList()),
                volunteer.getDescription(), volunteer.getActive());
    }

    public VolunteerDto(Long id, String name, String surname, Date dateOfBirth, String skypeName,
                        String email, List<String> languages,
                        String description, Boolean active) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.skypeName = skypeName;
        this.email = email;
        this.languages = languages;
        this.description = description;
        this.active = active;
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

    public String getSkypeName() {
        return skypeName;
    }

    public void setSkypeName(String skypeName) {
        this.skypeName = skypeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
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

}
