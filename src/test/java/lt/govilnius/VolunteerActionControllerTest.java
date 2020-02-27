package lt.govilnius;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.govilnius.domain.reservation.*;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.MeetService;
import lt.govilnius.facadeService.reservation.VolunteerService;
import lt.govilnius.repository.reservation.*;
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

import static lt.govilnius.EmailSenderTest.sampleVolunteer;
import static lt.govilnius.MeetServiceTest.sampleMeetDto;
import static lt.govilnius.MeetServiceTest.sampleVolunteerDto;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VolunteerActionControllerTest {

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
    private MeetAgeGroupRepository meetAgeGroupRepository;

    @Autowired
    private MeetLanguageRepository meetLanguageRepository;

    @Autowired
    private MeetService meetService;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private MeetEngagementService meetEngagementService;

    private static final String ERROR_MESSAGE = "Unfortunately, you've run out of time!";

    @After
    public void cleanEachTest() {
        meetAgeGroupRepository.findAll().forEach(age -> meetAgeGroupRepository.delete(age));
        meetLanguageRepository.findAll().forEach(meetLanguage -> meetLanguageRepository.delete(meetLanguage));
        meetEngagementRepository.findAll().forEach(volunteer -> meetEngagementRepository.delete(volunteer));
        meetRepository.findAll().forEach(meet -> meetRepository.delete(meet));
        volunteerRepository.findAll().forEach(volunteer -> volunteerRepository.delete(volunteer));
    }

    @Test
    public void agree_Token_ShoudOpenAgreement() throws Exception {
        MeetDto meetDto = sampleMeetDto();
        Meet meet = meetService.create(meetDto).get();

        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        VolunteerDto volunteerDto = sampleVolunteerDto();
        Volunteer volunteer = volunteerService.create(volunteerDto).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());

        MvcResult result = mvc.perform(get("/volunteer-action-management/agreements?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void agree_Token_ShoudOpenError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "TOKEN");

        MvcResult result = mvc.perform(get("/volunteer-action-management/agreements?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void cancel_Token_ShoudOpenCancellation() throws Exception {
        MeetDto meetDto = sampleMeetDto();
        Meet meet = meetService.create(meetDto).get();

        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        VolunteerDto volunteerDto = sampleVolunteerDto();
        Volunteer volunteer = volunteerService.create(volunteerDto).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());

        MvcResult result = mvc.perform(get("/volunteer-action-management/cancellations?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void cancel_Token_ShoudOpenError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "TOKEN");

        MvcResult result = mvc.perform(get("/volunteer-action-management/cancellations?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void editEngagement_Token_ShoudOpenChange() throws Exception {
        MeetDto meetDto = sampleMeetDto();
        Meet meet = meetService.create(meetDto).get();

        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        VolunteerDto volunteerDto = sampleVolunteerDto();
        Volunteer volunteer = volunteerService.create(volunteerDto).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());

        MvcResult result = mvc.perform(get("/volunteer-action-management/engagements?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    @Test
    public void editEngagement_Token_ShoudOpenError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "TOKEN");

        MvcResult result = mvc.perform(get("/volunteer-action-management/engagements?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }

    public void evaluate_PostToken_ShouldEvaluate() throws Exception {
        MeetDto meetDto = sampleMeetDto();
        Meet meet = meetService.create(meetDto).get();

        meet.setStatus(Status.FINISHED);
        meet = meetService.edit(meet.getId(), meet).get();
        meet = meetService.setFreezed(meet, false);

        VolunteerDto volunteerDto = sampleVolunteerDto();
        Volunteer volunteer = volunteerService.create(volunteerDto).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();
        meetEngagement.setConfirmed(true);
        meetEngagement = meetEngagementService.edit(meetEngagement.getId(), meetEngagement).get();

        MvcResult result = mvc.perform(post("/volunteer-action-management/evaluations")
                .param("comment", "comment")
                .param("token", meetEngagement.getToken()))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void evaluate_PostToken_ShouldOpenError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "TOKEN");
        params.put("comment", "comment");

        MvcResult result = mvc.perform(post("/volunteer-action-management/evaluations"))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE));
    }
}
