package lt.govilnius;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import lt.govilnius.domain.reservation.*;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import static lt.govilnius.MeetServiceTest.sampleMeetDto;
import static lt.govilnius.MeetServiceTest.sampleVolunteerDto;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "dev")
public class TouristActionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MeetService meetService;

    @Autowired
    private MeetEngagementService meetEngagementService;

    @Autowired
    private VolunteerService volunteerService;

    private static final String ERROR_MESSAGE_RUN_OUT_OF_TIME = "Unfortunately, you've run out of time!";
    private static final String ERROR_MESSAGE_CURRENTLY_SELECTED = "Unfortunately, you've currently selected the option!";

    @Autowired
    private MeetEngagementRepository meetEngagementRepository;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @After
    public void cleanEachTest() {
        meetEngagementRepository.findAll().forEach(volunteer -> meetEngagementRepository.delete(volunteer));
        meetRepository.findAll().forEach(meet -> meetRepository.delete(meet));
        volunteerRepository.findAll().forEach(volunteer -> volunteerRepository.delete(volunteer));
    }

    @Test
    public void select_Token_ShoudOpenCurrentlySelected() throws Exception {
        MeetDto meetDto = sampleMeetDto();
        Meet meet = meetService.create(meetDto).get();

        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();
        meet = meetService.setFreezed(meet, true);

        VolunteerDto volunteerDto = sampleVolunteerDto();
        Volunteer volunteer = volunteerService.create(volunteerDto).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();
        meetEngagement.setConfirmed(true);
        meetEngagement = meetEngagementService.edit(meetEngagement.getId(), meetEngagement).get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());

        MvcResult result = mvc.perform(get("/tourist/selections?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE_CURRENTLY_SELECTED));
    }

    @Test
    public void select_Token_ShoudOpenSelect() throws Exception {
        MeetDto meetDto = sampleMeetDto();
        Meet meet = meetService.create(meetDto).get();

        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();
        meet = meetService.setFreezed(meet, false);

        VolunteerDto volunteerDto = sampleVolunteerDto();
        Volunteer volunteer = volunteerService.create(volunteerDto).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();
        meetEngagement.setConfirmed(true);
        meetEngagement = meetEngagementService.edit(meetEngagement.getId(), meetEngagement).get();

        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());

        MvcResult result = mvc.perform(get("/tourist/selections?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE_RUN_OUT_OF_TIME));
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE_CURRENTLY_SELECTED));
    }

    @Test
    public void select_Token_ShoudOpenError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "TOKEN");

        MvcResult result = mvc.perform(get("/tourist/selections?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE_RUN_OUT_OF_TIME));
    }

    @Test
    public void evaluate_Token_ShoudOpenEvaluation() throws Exception {
        MeetDto meetDto = sampleMeetDto();
        Meet meet = meetService.create(meetDto).get();

        meet.setStatus(Status.SENT_TOURIST_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();
        meet = meetService.setFreezed(meet, false);

        VolunteerDto volunteerDto = sampleVolunteerDto();
        Volunteer volunteer = volunteerService.create(volunteerDto).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).get();
        Map<String, String> params = new HashMap<>();
        params.put("token", meetEngagement.getToken());

        MvcResult result = mvc.perform(get("/tourist/evaluations?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE_CURRENTLY_SELECTED));
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE_RUN_OUT_OF_TIME));
    }

    @Test
    public void evaluate_Token_ShoudOpenError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "TOKEN");

        MvcResult result = mvc.perform(get("/tourist/evaluations?token=" + params.get("token")))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE_CURRENTLY_SELECTED));
    }

    @Test
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

        MvcResult result = mvc.perform(post("/tourist/evaluations")
                .param("comment", "comment")
                .param("token", meetEngagement.getToken())
                .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE_CURRENTLY_SELECTED));
        Assert.assertFalse(actualResponseBody.contains(ERROR_MESSAGE_RUN_OUT_OF_TIME));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void evaluate_PostToken_ShouldOpenError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "TOKEN");
        params.put("comment", "comment");

        MvcResult result = mvc.perform(post("/tourist/evaluations")
                .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = result.getResponse().getContentAsString();
        Assert.assertTrue(actualResponseBody.contains(ERROR_MESSAGE_CURRENTLY_SELECTED));
    }
}
