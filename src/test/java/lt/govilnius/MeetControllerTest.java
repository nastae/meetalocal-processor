package lt.govilnius;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetEngagement;
import lt.govilnius.domain.reservation.Status;
import lt.govilnius.domain.reservation.Volunteer;
import lt.govilnius.domain.reservation.dto.TokenDto;
import lt.govilnius.facadeService.reservation.MeetEngagementService;
import lt.govilnius.facadeService.reservation.MeetService;
import lt.govilnius.facadeService.reservation.VolunteerService;
import lt.govilnius.repository.reservation.MeetEngagementRepository;
import lt.govilnius.repository.reservation.MeetRepository;
import lt.govilnius.repository.reservation.VolunteerRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static lt.govilnius.EmailSenderTest.sampleVolunteer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MeetControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MeetService meetService;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private MeetEngagementService meetEngagementService;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private MeetEngagementRepository meetEngagementRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).right().get();

        TokenDto tokenDto = new TokenDto(meetEngagement.getToken());

        Map<String, String> params = new HashMap<>();
        params.put("token", tokenDto.getToken());

        mvc.perform(post("/api/meet/agreements")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", params.get("token")))
                .andExpect(status().isOk());
    }

    @Test
    public void agree_Token_ShoudShowError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "RANDOM");

        mvc.perform(post("/api/meet/agreements")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", params.get("token")))
                .andExpect(status().isUnprocessableEntity());
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
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).right().get();

        TokenDto tokenDto = new TokenDto(meetEngagement.getToken());

        Map<String, String> params = new HashMap<>();
        params.put("token", tokenDto.getToken());

        mvc.perform(post("/api/meet/cancellations")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", params.get("token")))
                .andExpect(status().isOk());
    }

    @Test
    public void cancel_Token_ShoudShowError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "RANDOM");

        mvc.perform(post("/api/meet/cancellations")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", params.get("token")))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void change_Token_ShoudEdit() throws Exception {
        Meet meet = sampleMeet();
        meet = meetService.create(meet).get();

        meet.setStatus(Status.SENT_VOLUNTEER_REQUEST);
        meet = meetService.edit(meet.getId(), meet).get();

        Volunteer volunteer = sampleVolunteer();
        volunteer = volunteerService.create(volunteer).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).right().get();

        TokenDto tokenDto = new TokenDto(meetEngagement.getToken());

        Map<String, String> params = new HashMap<>();
        params.put("token", tokenDto.getToken());
        params.put("time", "20:20:20");

        mvc.perform(post("/api/meet/changes")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", params.get("token"))
                .param("time", params.get("time")))
                .andExpect(status().isOk());
    }

    @Test
    public void change_Token_ShoudShowError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "RANDOM");
        params.put("time", "20:20:20");

        mvc.perform(post("/api/meet/changes")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", params.get("token"))
                .param("time", params.get("time")))
                .andExpect(status().isUnprocessableEntity());
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
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).right().get();

        TokenDto tokenDto = new TokenDto(meetEngagement.getToken());

        Map<String, String> params = new HashMap<>();
        params.put("token", tokenDto.getToken());
        params.put("comment", "comment");

        mvc.perform(post("/api/meet/reports")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", params.get("token"))
                .param("comment", params.get("comment")))
                .andExpect(status().isOk());
    }

    @Test
    public void report_Token_ShoudShowError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "RANDOM");
        params.put("comment", "comment");

        mvc.perform(post("/api/meet/reports")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", params.get("token"))
                .param("comment", params.get("comment")))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void changeAfterAddition_Token_ShoudEdit() throws Exception {
        Meet meet = sampleMeet();
        meet = meetService.create(meet).get();

        meet.setStatus(Status.SENT_VOLUNTEER_ADDITION);
        meet = meetService.edit(meet.getId(), meet).get();

        Volunteer volunteer = sampleVolunteer();
        volunteer = volunteerService.create(volunteer).get();

        Time time = new Time(10, 10, 10);
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).right().get();

        TokenDto tokenDto = new TokenDto(meetEngagement.getToken());

        Map<String, String> params = new HashMap<>();
        params.put("token", tokenDto.getToken());
        params.put("time", "20:20:20");

        mvc.perform(post("/api/meet/changes-after-addition")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", params.get("token"))
                .param("time", params.get("time")))
                .andExpect(status().isOk());
    }

    @Test
    public void changeAfterAddition_Token_ShoudShowError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "RANDOM");
        params.put("time", "20:20:20");

        mvc.perform(post("/api/meet/changes-after-addition")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", params.get("token"))
                .param("time", params.get("time")))
                .andExpect(status().isUnprocessableEntity());
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
        MeetEngagement meetEngagement = meetEngagementService.create(meet, volunteer, time).right().get();

        meetEngagement.setEngaged(true);
        meetEngagement = meetEngagementService.edit(meetEngagement.getId(), meetEngagement).get();

        TokenDto tokenDto = new TokenDto(meetEngagement.getToken());

        Map<String, String> params = new HashMap<>();
        params.put("token", tokenDto.getToken());

        mvc.perform(post("/api/meet/selections")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", params.get("token")))
                .andExpect(status().isOk());
    }

    @Test
    public void select_Token_ShoudShowError() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("token", "RANDOM");

        mvc.perform(post("/api/meet/selections")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("token", params.get("token")))
                .andExpect(status().isUnprocessableEntity());
    }
}
