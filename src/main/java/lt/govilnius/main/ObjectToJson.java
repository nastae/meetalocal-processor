package lt.govilnius.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.govilnius.models.Gender;
import lt.govilnius.models.MeetingForm;

import java.io.IOException;
import java.util.Calendar;

public class ObjectToJson {

    public static void main(String[] a)
    {
        MeetingForm form = new MeetingForm();
        form = getObjectData(form);
        ObjectMapper Obj = new ObjectMapper();

        try {
            String jsonStr = Obj.writeValueAsString(form);
            System.out.println(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MeetingForm getObjectData(MeetingForm form) {
        form.setEmail("aurisgo1998@gmail.com");
        form.setPhoneNumber("862564713");
        form.setNameAndLastname("Aurimas Golotylecas");
        form.setFromCountry("Lietuva");
        form.setMeetingDate(Calendar.getInstance().getTime());
        form.setPeopleCount(4);
        form.setPreferences("Preferences");
        form.setGender(Gender.MALE.getName());
        form.setAge(18);
        return form;
    }
}
