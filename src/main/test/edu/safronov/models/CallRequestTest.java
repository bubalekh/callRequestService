package edu.safronov.models;

import edu.safronov.domain.CallRequest;
import org.junit.jupiter.api.Test;

import java.util.*;

class CallRequestTest {

    private final Calendar testDate = Calendar.getInstance();
    private final CallRequest testObject = new CallRequest();

    /*@Test
    void generateAvailableTimeTest() {
        List<String> testTime = new ArrayList<>(Arrays.asList("09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"));
        testDate.setTime(new Date(0)); //48960000 for 17:00, 0 for everything
        //System.out.println(testDate.getTime());
        testObject.getDateTime().toLocalDate().(testDate.getTime());
        //System.out.println(testObject.generateAvailableTime(testDate));
        assert testObject.generateAvailableTime(testDate).equals(testTime);
    }

    @Test
    void generateAvailableTimeNineAmTest() {
        List<String> testTime = new ArrayList<>(Arrays.asList("09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"));
        testDate.setTime(new Date(21500000)); //48960000 for 17:00, 0 for everything
        //System.out.println(testDate.getTime());
        testObject.getDateTime().setTime(testDate.getTime());
        //System.out.println(testObject.generateAvailableTime(testDate));
        assert testObject.generateAvailableTime(testDate).equals(testTime);
    }

    @Test
    void generateAvailableTimeTwelveTest() {
        List<String> testTime = new ArrayList<>(Arrays.asList("13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"));
        testDate.setTime(new Date(32400000)); //48960000 for 17:00, 0 for everything
        //System.out.println(testDate.getTime());
        testObject.getDateTime().setTime(testDate.getTime());
        //System.out.println(testObject.generateAvailableTime(testDate));
        assert testObject.generateAvailableTime(testDate).equals(testTime);
    }*/
}