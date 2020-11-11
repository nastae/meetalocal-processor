package lt.govilnius;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.govilnius.domain.reservation.LiveMeet;
import lt.govilnius.domain.reservation.LiveMeetDto;
import lt.govilnius.domain.reservation.OnlineMeet;
import lt.govilnius.domain.reservation.OnlineMeetDto;
import lt.govilnius.repository.reservation.LiveMeetRepository;
import lt.govilnius.repository.reservation.OnlineMeetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static lt.govilnius.LiveEmailSenderTest.sampleLiveMeet;
import static lt.govilnius.MeetServiceTest.sampleLiveMeetDto;
import static lt.govilnius.MeetServiceTest.sampleOnlineMeetDto;
import static lt.govilnius.OnlineEmailSenderTest.sampleOnlineMeet;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "dev")
public class OnlineMeetControllerTest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private OnlineMeetRepository onlineMeetRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "/online-meet/meets";

    @Test
    public void newMeet_Meet_ShoudCreateNew() throws Exception {
        OnlineMeetDto meet = sampleOnlineMeetDto();

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
                .andExpect(jsonPath("$.skypeName").value(meet.getSkypeName()))
                .andExpect(jsonPath("$.country").value(meet.getCountry()))
                .andExpect(jsonPath("$.date").value(meet.getDate().toString()))
                .andExpect(jsonPath("$.time").value(meet.getTime().toString()))
                .andExpect(jsonPath("$.age").value(meet.getAge()))
                .andExpect(jsonPath("$.preferences").value(meet.getPreferences()))
                .andExpect(jsonPath("$.additionalPreferences").value(meet.getAdditionalPreferences()));
    }

    @Test
    public void newMeet_MeetWithNullEmail_ShoudShowBadRequest() throws Exception {
        OnlineMeet meet = sampleOnlineMeet();
        meet.setEmail(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullSkypeName_ShoudShowBadRequest() throws Exception {
        OnlineMeet meet = sampleOnlineMeet();
        meet.setSkypeName(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullName_ShoudShowBadRequest() throws Exception {
        OnlineMeet meet = sampleOnlineMeet();
        meet.setName(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullSurname_ShoudShowBadRequest() throws Exception {
        OnlineMeet meet = sampleOnlineMeet();
        meet.setSurname(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullResidence_ShoudShowBadRequest() throws Exception {
        OnlineMeet meet = sampleOnlineMeet();
        meet.setCountry(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullDate_ShoudShowBadRequest() throws Exception {
        OnlineMeet meet = sampleOnlineMeet();
        meet.setDate(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullTime_ShoudShowBadRequest() throws Exception {
        OnlineMeet meet = sampleOnlineMeet();
        meet.setTime(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullAge_ShoudShowBadRequest() throws Exception {
        OnlineMeet meet = sampleOnlineMeet();
        meet.setAge(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void newMeet_MeetWithNullAgeGroup_ShoudShowBadRequest() throws Exception {
        OnlineMeet meet = sampleOnlineMeet();
        meet.setMeetAgeGroups(null);

        mvc.perform(post(URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(meet)))
                .andExpect(status().isBadRequest());
    }
}
