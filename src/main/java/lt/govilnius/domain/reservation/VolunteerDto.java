package lt.govilnius.domain.reservation;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class VolunteerDto {

    private Long id;

    private String name;

    private String surname;

    private Date dateOfBirth;

    private String phoneNumber;

    private String email;

    private List<String> languages = new ArrayList<>();

    private String description;

    private Boolean active = true;

    private List<String> purposes = new ArrayList<>();

    public VolunteerDto() {}

    public VolunteerDto(Volunteer volunteer) {
        this(volunteer.getId(), volunteer.getName(), volunteer.getSurname(), volunteer.getDateOfBirth(), volunteer.getPhoneNumber(),
                volunteer.getEmail(), volunteer.getLanguages().stream().map(l -> l.getLanguage().getName()).collect(toList()),
                volunteer.getDescription(), volunteer.getActive(),
                volunteer.getPurposes().stream().map(l -> l.getPurpose().getName()).collect(toList()));
    }

    public VolunteerDto(Long id, String name, String surname, Date dateOfBirth, String phoneNumber,
                        String email, List<String> languages,
                        String description, Boolean active, List<String> purposes) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.languages = languages;
        this.description = description;
        this.active = active;
        this.purposes = purposes;
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

    public List<String> getPurposes() {
        return purposes;
    }

    public void setPurposes(List<String> purposes) {
        this.purposes = purposes;
    }
}
