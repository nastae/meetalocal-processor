package lt.govilnius;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.MeetService;
import lt.govilnius.facadeService.reservation.VolunteerService;
import lt.govilnius.repository.reservation.MeetEngagementRepository;
import lt.govilnius.repository.reservation.MeetRepository;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static lt.govilnius.EmailSenderTest.sampleVolunteer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MeetStatusControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private MeetEngagementRepository meetEngagementRepository;

    @Autowired
    private MeetService meetService;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private MeetEngagementService meetEngagementService;

    private static final String ERROR_MESSAGE = "The action is not available!";

    @After
    public void cleanEachTest() {
        meetEngagementRepository.findAll().forEach(volunteer -> meetEngagementRepository.delete(volunteer));
        meetRepository.findAll().forEach(meet -> meetRepository.delete(meet));
        volunteerRepository.findAll().forEach(volunteer -> volunteerRepository.delete(volunteer));
    }

    @Test
    public void agree_Token_ShoudEdit() throws Exception {
        Meet meet = sampleMeet();
        meet = meetService.create(meet).get();

        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        Volunteer volunteer = sampleVolunteer();
        volunteer = volunteerService.create(volunteer).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());

        MvcResult result = mvc.perform(post("/meet/agreements")
                .param("token", params.get("token")))
                .andExpect(status().isOk())
                .andReturn();;
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void agree_Token_ShoudShowError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "RANDOM");

        MvcResult result =  mvc.perform(post("/meet/agreements")
                .param("token", params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void cancel_Token_ShoudEdit() throws Exception {
        Meet meet = sampleMeet();
        meet = meetService.create(meet).get();

        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        Volunteer volunteer = sampleVolunteer();
        volunteer = volunteerService.create(volunteer).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());

        MvcResult result = mvc.perform(post("/meet/cancellations")
                .param("token", params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void cancel_Token_ShoudShowError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "RANDOM");

        MvcResult result = mvc.perform(post("/meet/cancellations")
                .param("token", params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void changeEngagement_Token_ShoudEdit() throws Exception {
        Meet meet = sampleMeet();
        meet = meetService.create(meet).get();

        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        Volunteer volunteer = sampleVolunteer();
        volunteer = volunteerService.create(volunteer).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());
        params.put("time", "20:20:20");

        MvcResult result = mvc.perform(post("/meet/engagements-changes")
                .param("token", params.get("token"))
                .param("time", params.get("time")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void changeEngagement_Token_ShoudShowError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "RANDOM");
        params.put("time", "20:20:20");

        MvcResult result = mvc.perform(post("/meet/engagements-changes")
                .param("token", params.get("token"))
                .param("time", params.get("time")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void report_Token_ShoudEdit() throws Exception {
        Meet meet = sampleMeet();
        meet = meetService.create(meet).get();

        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        Volunteer volunteer = sampleVolunteer();
        volunteer = volunteerService.create(volunteer).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());
        params.put("comment", "comment");

        MvcResult result = mvc.perform(post("/meet/reports")
                .param("token", params.get("token"))
                .param("comment", params.get("comment")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void report_Token_ShoudShowError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "RANDOM");
        params.put("comment", "comment");

        MvcResult result = mvc.perform(post("/meet/reports")
                .param("token", params.get("token"))
                .param("comment", params.get("comment")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void changeMeetAfterAddition_Token_ShoudEdit() throws Exception {
        Meet meet = sampleMeet();
        meet = meetService.create(meet).get();

        meet.setStatus(Status.SENT_TOURIST_ADDITION);
        meet = meetService.edit(meet.getId(), meet).get();

        Volunteer volunteer = sampleVolunteer();
        volunteer = volunteerService.create(volunteer).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();

        Map<String, String> params = new HashMap<>();
        params.put("meet", meet.getId().toString());
        params.put("time", "20:20:20");

        MvcResult result = mvc.perform(post("/meet/meets-changes")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("meet", params.get("meet"))
                .param("time", params.get("time")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void changeMeetAfterAddition_Token_ShoudShowError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("meet", "10");
        params.put("time", "20:20:20");

        MvcResult result = mvc.perform(post("/meet/meets-changes")
                .param("meet", params.get("meet"))
                .param("time", params.get("time")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void select_Token_ShoudEdit() throws Exception {
        Meet meet = sampleMeet();
        meet = meetService.create(meet).get();

        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        Volunteer volunteer = sampleVolunteer();
        volunteer = volunteerService.create(volunteer).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();

        meetEngagement.setConfirmed(true);
        meetEngagement = meetEngagementService.edit(meetEngagement.getId(), meetEngagement).get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());

        MvcResult result = mvc.perform(post("/meet/selections")
                .param("token", params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void select_Token_ShoudShowError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "RANDOM");

        MvcResult result = mvc.perform(post("/meet/selections")
                .param("token", params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }
}
