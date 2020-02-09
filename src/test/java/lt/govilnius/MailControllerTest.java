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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static lt.govilnius.EmailSenderTest.sampleVolunteer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MailControllerTest {

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

    private static final String ERROR_MESSAGE = "Something went wrong!";

    @After
    public void cleanEachTest() {
        meetEngagementRepository.findAll().forEach(volunteer -> meetEngagementRepository.delete(volunteer));
        meetRepository.findAll().forEach(meet -> meetRepository.delete(meet));
        volunteerRepository.findAll().forEach(volunteer -> volunteerRepository.delete(volunteer));
    }

    @Test
    public void agree_Token_ShoudOpenAgreement() throws Exception {
        Meet meet = sampleMeet();
        meet = meetService.create(meet).get();

        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        Volunteer volunteer = sampleVolunteer();
        volunteer = volunteerService.create(volunteer).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).right().get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());

        MvcResult result = mvc.perform(get("/mail/agreements?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void agree_Token_ShoudOpenError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "TOKEN");

        MvcResult result = mvc.perform(get("/mail/agreements?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void cancel_Token_ShoudOpenCancellation() throws Exception {
        Meet meet = sampleMeet();
        meet = meetService.create(meet).get();

        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        Volunteer volunteer = sampleVolunteer();
        volunteer = volunteerService.create(volunteer).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).right().get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());

        MvcResult result = mvc.perform(get("/mail/cancellations?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void cancel_Token_ShoudOpenError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "TOKEN");

        MvcResult result = mvc.perform(get("/mail/cancellations?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void changeEngagement_Token_ShoudOpenChange() throws Exception {
        Meet meet = sampleMeet();
        meet = meetService.create(meet).get();

        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        Volunteer volunteer = sampleVolunteer();
        volunteer = volunteerService.create(volunteer).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).right().get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());

        MvcResult result = mvc.perform(get("/mail/engagements-changes?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void changeEngagement_Token_ShoudOpenError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "TOKEN");

        MvcResult result = mvc.perform(get("/mail/engagements-changes?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void report_Token_ShoudOpenReport() throws Exception {
        Meet meet = sampleMeet();
        meet = meetService.create(meet).get();

        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        Volunteer volunteer = sampleVolunteer();
        volunteer = volunteerService.create(volunteer).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).right().get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());

        MvcResult result = mvc.perform(get("/mail/reports?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void report_Token_ShoudOpenError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "TOKEN");

        MvcResult result = mvc.perform(get("/mail/reports?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void changeMeets_Token_ShoudOpenMeet() throws Exception {
        Meet meet = sampleMeet();
        meet = meetService.create(meet).get();

        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        Map<String, String> params = new HashMap<>();
        params.put("meet", meet.getId().toString());

        MvcResult result = mvc.perform(get("/mail/meets-changes?meet=" + params.get("meet")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void changeMeets_Token_ShoudOpenError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("meet", "10");

        MvcResult result = mvc.perform(get("/mail/meets-changes?meet=" + params.get("meet")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void select_Token_ShoudOpenSelect() throws Exception {
        Meet meet = sampleMeet();
        meet = meetService.create(meet).get();

        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        Volunteer volunteer = sampleVolunteer();
        volunteer = volunteerService.create(volunteer).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).right().get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());

        MvcResult result = mvc.perform(get("/mail/selections?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void select_Token_ShoudOpenError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "TOKEN");

        MvcResult result = mvc.perform(get("/mail/selections?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }
}
