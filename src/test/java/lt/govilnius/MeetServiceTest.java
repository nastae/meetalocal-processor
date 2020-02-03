package lt.govilnius;

import io.atlassian.fugue.Either;
import lt.govilnius.domain.reservation.*;
import lt.govilnius.facadeService.reservation.MeetService;
import lt.govilnius.repository.reservation.MeetRepository;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static lt.govilnius.EmailSenderTest.sampleVolunteer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MeetServiceTest {

    @Autowired
    private MeetService meetService;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private MeetRepository meetRepository;

    @After
    public void cleanEachTest() {
        meetRepository.findAll().forEach(meet -> meetRepository.delete(meet));
        volunteerRepository.findAll().forEach(volunteer -> volunteerRepository.delete(volunteer));
    }

    @Test
    public void create_Meet_ShouldBeCreated() {
        Meet meet = sampleMeet();
        Meet newMeet = meetService.create(meet).right().get();
        Assert.assertEquals(meet.getName(), newMeet.getName());
        Assert.assertEquals(meet.getSurname(), newMeet.getSurname());
        Assert.assertEquals(meet.getPhoneNumber(), newMeet.getPhoneNumber());
        Assert.assertEquals(meet.getResidence(), newMeet.getResidence());
        Assert.assertEquals(meet.getDate(), newMeet.getDate());
        Assert.assertEquals(meet.getTime(), newMeet.getTime());
        Assert.assertEquals(meet.getPeopleCount(), newMeet.getPeopleCount());
        Assert.assertEquals(meet.getAge(), newMeet.getAge());
        Assert.assertEquals(meet.getAgeGroup(), newMeet.getAgeGroup());
        Assert.assertEquals(newMeet.getLanguages(), meet.getLanguages());
        Assert.assertEquals(meet.getPreferences(), newMeet.getPreferences());
        Assert.assertEquals(meet.getComment(), newMeet.getComment());
        Assert.assertEquals(meet.getStatus(), newMeet.getStatus());
        Assert.assertEquals(meet.getVolunteer(), newMeet.getVolunteer());
    }

    @Test
    public void getAll_Meets_ShouldGet() {
        meetService.create(sampleMeet());
        List<Meet> meets = meetService.getAll();
        Assert.assertEquals(meets.size(), 1);
    }

    @Test
    public void get_Meet_ShouldGetById() {
        meetService.create(sampleMeet());
        List<Meet> meets = meetService.getAll();
        Optional<Meet> meet = meetService.get(meets.get(0).getId());
        Assert.assertTrue(meet.isPresent());
    }

    @Test
    public void get_Meet_ShouldNotGetById() {
        meetService.create(sampleMeet());
        List<Meet> meets = meetService.getAll();
        Optional<Meet> meet = meetService.get(meets.get(0).getId()+1);
        Assert.assertFalse(meet.isPresent());
    }

    @Test
    public void addVolunteer_Volunteer_ShouldBeAddedVolunteer() {
        meetService.create(sampleMeet());
        volunteerRepository.save(sampleVolunteer());
        List<Meet> meets = meetService.getAll();
        Volunteer newVolunteer = volunteerRepository.findAll().get(0);

        Meet meet = meetService.addVolunteer(meets.get(0).getId(), newVolunteer.getId()).right().get();
        Assert.assertEquals(meet.getVolunteer().getId(), newVolunteer.getId());
    }

    @Test
    public void addVolunteer_ExistingVolunteer_ShouldNotBeAddedVolunteer() {
        meetService.create(sampleMeet());
        volunteerRepository.save(sampleVolunteer());
        List<Meet> meets = meetService.getAll();
        Volunteer newVolunteer = volunteerRepository.findAll().get(0);
        Meet meet = meetService.addVolunteer(meets.get(0).getId(), newVolunteer.getId()).right().get();

        Either<Exception, Meet> meetEither = meetService.addVolunteer(meets.get(0).getId(), newVolunteer.getId());
        Assert.assertTrue(meetEither.isLeft());
        Assert.assertFalse(meetEither.left().isEmpty());
    }

    @Test
    public void addVolunteer_NotExistingMeet_ShouldNotBeAdded() {
        Either<Exception, Meet> meetEither = meetService.addVolunteer(0L, 0L);
        Assert.assertTrue(meetEither.isLeft());
        Assert.assertFalse(meetEither.left().isEmpty());
    }

    @Test
    public void addLanguage_Language_ShouldBeAddedLanguage() {
        meetService.create(sampleMeet());
        Meet meet = meetService.getAll().get(0);
        MeetLanguage language = meetService.addLanguage(meet.getId(), Language.ENGLISH).right().get();
        Assert.assertEquals(language.getLanguage(), Language.ENGLISH);
        meet = meetService.get(meet.getId()).get();
        Assert.assertEquals(meet.getLanguages().size(), 1);
    }

    @Test
    public void addLanguage_NotExistingMeet_SShouldNotBeAdded() {
        Either<Exception, MeetLanguage> languageEither = meetService.addLanguage(0L, Language.ENGLISH);
        Assert.assertTrue(languageEither.isLeft());
        Assert.assertFalse(languageEither.left().isEmpty());
    }

    @Test
    public void delete_Meet_ShouldBeDeleted() {
        meetService.create(sampleMeet());
        List<Meet> meets = meetService.getAll();
        Assert.assertEquals(meets.size(), 1);
        final Meet meet = meets.get(0);
        meetService.delete(meet.getId());
        meets = meetService.getAll();
        Assert.assertEquals(meets.size(), 0);
    }

    @Test
    public void edit_Meet_ShouldEdit() {
        Meet meet = meetService.create(sampleMeet()).right().get();
        Assert.assertEquals(meet.getStatus(), Status.NEW);
        meet.setStatus(Status.REPORTED);
        Meet newMeet = meetService.edit(meet.getId(), meet).get();
        Assert.assertEquals(newMeet.getStatus(), Status.REPORTED);
        Assert.assertNotEquals(meet.getChangedAt(), newMeet.getChangedAt());
    }

    @Test
    public void edit_NotExistingMeet_ShouldNotEdit() {
        Optional<Meet> meetOptional = meetService.edit(0L, null);
        Assert.assertFalse(meetOptional.isPresent());
    }
}