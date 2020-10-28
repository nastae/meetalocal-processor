package lt.govilnius;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.govilnius.domain.reservation.Meet;
import lt.govilnius.domain.reservation.MeetDto;
import lt.govilnius.repository.reservation.MeetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static lt.govilnius.EmailSenderTest.sampleMeet;
import static lt.govilnius.MeetServiceTest.sampleMeetDto;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "dev")
public class MeetRegistrationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "/registration/meets";

    @Test
    public void newMeet_Meet_ShoudCreateNew() throws Exception {
        MeetDto meet = sampleMeetDto();

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(meet.getName()))
                .andExpect(jsonPath("$.surname").value(meet.getSurname()))
                .andExpect(jsonPath("$.email").value(meet.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(meet.getPhoneNumber()))
                .andExpect(jsonPath("$.country").value(meet.getCountry()))
                .andExpect(jsonPath("$.date").value(meet.getDate().toString()))
                .andExpect(jsonPath("$.time").value(meet.getTime().toString()))
                .andExpect(jsonPath("$.peopleCount").value(meet.getPeopleCount()))
                .andExpect(jsonPath("$.age").value(meet.getAge()))
                .andExpect(jsonPath("$.preferences").value(meet.getPreferences()))
                .andExpect(jsonPath("$.additionalPreferences").value(meet.getAdditionalPreferences()));
    }

    @Test
    public void newMeet_MeetWithNullEmail_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setEmail(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullPhoneNumber_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setPhoneNumber(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullName_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setName(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullSurname_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setSurname(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullResidence_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setCountry(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullDate_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setDate(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullTime_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setTime(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullPeopleCount_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setPeopleCount(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullAge_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setAge(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullAgeGroup_ShoudShowBadRequest() throws Exception {
        Meet meet = sampleMeet();
        meet.setMeetAgeGroups(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }
}
