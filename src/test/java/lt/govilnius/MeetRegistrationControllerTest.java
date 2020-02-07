package lt.govilnius;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.repository.reservation.MeetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MeetRegistrationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void newMeet_Meet_ShoudCreateNew() throws Exception {
        Meet meet = sampleMeet();

        mvc.perform(post("/api/meets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(meet.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(meet.getPhoneNumber()))
                .andExpect(jsonPath("$.name").value(meet.getName()))
                .andExpect(jsonPath("$.surname").value(meet.getSurname()))
                .andExpect(jsonPath("$.residence").value(meet.getResidence()))
                .andExpect(jsonPath("$.date").value(meet.getDate().toString()))
                .andExpect(jsonPath("$.time").value(meet.getTime().toString()))
                .andExpect(jsonPath("$.peopleCount").value(meet.getPeopleCount()))
                .andExpect(jsonPath("$.age").value(meet.getAge()))
                .andExpect(jsonPath("$.gender").value(meet.getGender().toString()))
                .andExpect(jsonPath("$.ageGroup").value(meet.getAgeGroup().toString()))
                .andExpect(jsonPath("$.preferences").value(meet.getPreferences()))
                .andExpect(jsonPath("$.comment").value(meet.getComment()))
                .andExpect(jsonPath("$.status").value(meet.getStatus().toString()))
                .andExpect(jsonPath("$.volunteer").value(meet.getVolunteer()))
                .andExpect(jsonPath("$.languages[0].language").value(new ArrayList<>(meet.getLanguages()).get(0).getLanguage().toString()));
    }

    @Test
    public void newMeet_MeetWithNullEmail_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setEmail(null);

        mvc.perform(post("/api/meets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullPhoneNumber_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setPhoneNumber(null);

        mvc.perform(post("/api/meets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullName_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setName(null);

        mvc.perform(post("/api/meets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullSurname_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setSurname(null);

        mvc.perform(post("/api/meets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullResidence_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setResidence(null);

        mvc.perform(post("/api/meets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullDate_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setDate(null);

        mvc.perform(post("/api/meets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullTime_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setTime(null);

        mvc.perform(post("/api/meets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullPeopleCount_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setPeopleCount(null);

        mvc.perform(post("/api/meets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullAge_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setAge(null);

        mvc.perform(post("/api/meets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullGender_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setGender(null);

        mvc.perform(post("/api/meets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullAgeGroup_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setAgeGroup(null);

        mvc.perform(post("/api/meets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }
}
