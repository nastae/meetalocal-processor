package lt.govilnius;

import io.atlassian.fugue.Either;
import lt.govilnius.domain.reservation.Gender;
import lt.govilnius.domain.reservation.Language;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.domain.reservation.VolunteerLanguage;
import lt.govilnius.facadeService.reservation.VolunteerService;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static lt.govilnius.EmailSenderTest.sampleVolunteer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VolunteerServiceTest {

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @After
    public void cleanEachTest() {
        volunteerRepository.findAll().forEach(volunteer -> volunteerRepository.delete(volunteer));
    }

    @Test
    public void create_Volunteer_ShouldBeCreated() {
        Volunteer volunteer = sampleVolunteer();
        Volunteer newVolunteer = volunteerService.create(volunteer).right().get();
        Assert.assertNotEquals(volunteer.getCreatedAt(), newVolunteer.getCreatedAt());
        Assert.assertNotEquals(volunteer.getChangedAt(), newVolunteer.getChangedAt());
        Assert.assertEquals(volunteer.getName(), newVolunteer.getName());
        Assert.assertEquals(volunteer.getSurname(), newVolunteer.getSurname());
        Assert.assertEquals(volunteer.getDateOfBirth(), newVolunteer.getDateOfBirth());
        Assert.assertEquals(volunteer.getPhoneNumber(), newVolunteer.getPhoneNumber());
        Assert.assertEquals(volunteer.getEmail(), newVolunteer.getEmail());
        Assert.assertEquals(volunteer.getLanguages(), newVolunteer.getLanguages());
        Assert.assertEquals(volunteer.getAdditionalLanguages(), newVolunteer.getAdditionalLanguages());
        Assert.assertEquals(volunteer.getAge(), newVolunteer.getAge());
        Assert.assertEquals(volunteer.getGender(), newVolunteer.getGender());
        Assert.assertEquals(volunteer.getDescription(), newVolunteer.getDescription());
        Assert.assertEquals(volunteer.getActive(), newVolunteer.getActive());
    }

    @Test
    public void getAll_Volunteers_ShouldGet() {
        volunteerService.create(sampleVolunteer());
        List<Volunteer> volunteers = volunteerService.getAll();
        Assert.assertEquals(volunteers.size(), 1);
    }

    @Test
    public void get_Volunteer_ShouldGetById() {
        volunteerService.create(sampleVolunteer());
        List<Volunteer> volunteers = volunteerService.getAll();
        Optional<Volunteer> volunteer = volunteerService.get(volunteers.get(0).getId());
        Assert.assertTrue(volunteer.isPresent());
    }

    @Test
    public void get_Volunteer_ShouldNotGetById() {
        volunteerService.create(sampleVolunteer());
        List<Volunteer> volunteers = volunteerService.getAll();
        Optional<Volunteer> volunteer = volunteerService.get(volunteers.get(0).getId()+1);
        Assert.assertFalse(volunteer.isPresent());
    }

    @Test
    public void addLanguage_Language_ShouldBeAddedLanguage() {
        volunteerService.create(sampleVolunteer());
        Volunteer volunteer = volunteerService.getAll().get(0);
        VolunteerLanguage language = volunteerService.addLanguage(volunteer.getId(), Language.ENGLISH).right().get();
        Assert.assertEquals(language.getLanguage(), Language.ENGLISH);
        volunteer = volunteerService.get(volunteer.getId()).get();
        Assert.assertEquals(volunteer.getLanguages().size(), 1);
    }

    @Test
    public void addLanguage_NotExistingVolunteer_SShouldNotBeAdded() {
        Either<Exception, VolunteerLanguage> languageEither = volunteerService.addLanguage(0L, Language.ENGLISH);
        Assert.assertTrue(languageEither.isLeft());
        Assert.assertFalse(languageEither.left().isEmpty());
    }

    @Test
    public void delete_Volunteer_ShouldBeDeleted() {
        volunteerService.create(sampleVolunteer());
        List<Volunteer> volunteers = volunteerService.getAll();
        Assert.assertEquals(volunteers.size(), 1);
        final Volunteer volunteer = volunteers.get(0);
        volunteerService.delete(volunteer.getId());
        volunteers = volunteerService.getAll();
        Assert.assertEquals(volunteers.size(), 0);
    }

    @Test
    public void edit_Volunteer_ShouldEdit() {
        Volunteer volunteer = volunteerService.create(sampleVolunteer()).right().get();
        volunteer.setName("NEW");
        volunteer.setSurname("NEW");
        volunteer.setDateOfBirth(new Date(1999, 1, 1));
        volunteer.setPhoneNumber("987624");
        volunteer.setEmail("test@gmail.com");
        volunteer.setAdditionalLanguages("Spain");
        volunteer.setAge(22);
        volunteer.setGender(Gender.OTHER);
        volunteer.setDescription("NEW");
        volunteer.setActive(false);
        Volunteer newVolunteer = volunteerService.edit(volunteer.getId(), volunteer).get();
        Assert.assertNotEquals(volunteer.getChangedAt(), newVolunteer.getChangedAt());
        Assert.assertEquals(volunteer.getName(), newVolunteer.getName());
        Assert.assertEquals(volunteer.getSurname(), newVolunteer.getSurname());
        Assert.assertEquals(volunteer.getDateOfBirth(), newVolunteer.getDateOfBirth());
        Assert.assertEquals(volunteer.getPhoneNumber(), newVolunteer.getPhoneNumber());
        Assert.assertEquals(volunteer.getEmail(), newVolunteer.getEmail());
        Assert.assertEquals(volunteer.getLanguages(), newVolunteer.getLanguages());
        Assert.assertEquals(volunteer.getAdditionalLanguages(), newVolunteer.getAdditionalLanguages());
        Assert.assertEquals(volunteer.getAge(), newVolunteer.getAge());
        Assert.assertEquals(volunteer.getGender(), newVolunteer.getGender());
        Assert.assertEquals(volunteer.getDescription(), newVolunteer.getDescription());
        Assert.assertEquals(volunteer.getActive(), newVolunteer.getActive());
    }

    @Test
    public void edit_NotExistingVolunteer_ShouldNotEdit() {
        Optional<Volunteer> volunteerOptional = volunteerService.edit(0L, null);
        Assert.assertFalse(volunteerOptional.isPresent());
    }

    @Test
    public void findActive_Volunteers_ShouldGet() {
        Assert.assertEquals(volunteerService.findActive().size(), 0);
        volunteerService.create(sampleVolunteer());
        Assert.assertEquals(volunteerService.findActive().size(), 1);
    }
}
