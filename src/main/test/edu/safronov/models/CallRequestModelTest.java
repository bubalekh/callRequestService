package edu.safronov.models;

import org.junit.jupiter.api.Test;

import java.util.*;

class CallRequestModelTest {

    private final Calendar testDate = Calendar.getInstance();
    private final CallRequestModel testObject = new CallRequestModel();

    @Test
    void addTimeToDateTest() {
        testDate.setTime(new Date(0));
        String testTime = "12:00";
        testObject.getCalendar().setTime(testDate.getTime());
        testObject.addTimeToDate(testTime);
        testDate.set(Calendar.HOUR_OF_DAY, 12);
        assert testObject.getCalendar().getTime().equals(testDate.getTime());
    }

    @Test
    void addTimeToDateZeroTest() {
        testDate.setTime(new Date(0));
        String testTime = "0";
        testObject.getCalendar().setTime(testDate.getTime());
        testObject.addTimeToDate(testTime);
        assert testObject.getCalendar().getTime().equals(testDate.getTime());
    }

    /*@Test
    void ceilTimeTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        testDate.setTime(new Date(45000000)); //12:30 1 Jan 1970 UTC
        var testTime = (testDate.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + testDate.get(Calendar.HOUR_OF_DAY) : testDate.get(Calendar.HOUR_OF_DAY))
                + ":"
                + (testDate.get(Calendar.MINUTE) < 10 ? "0" + testDate.get(Calendar.MINUTE) : testDate.get(Calendar.MINUTE));
        Method ceilTime = CallRequestModel.class.getDeclaredMethod("ceilTime", Calendar.class);
        ceilTime.setAccessible(true);
        String timeToTest = (String) ceilTime.invoke(testObject, testDate);
        assert testTime.equals(timeToTest);
    }*/

    @Test
    void generateAvailableTimeTest() {
        List<String> testTime = new ArrayList<>(Arrays.asList("09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"));
        testDate.setTime(new Date(0)); //48960000 for 17:00, 0 for everything
        //System.out.println(testDate.getTime());
        testObject.getCalendar().setTime(testDate.getTime());
        //System.out.println(testObject.generateAvailableTime(testDate));
        assert testObject.generateAvailableTime(testDate).equals(testTime);
    }

    @Test
    void generateAvailableTimeNineAmTest() {
        List<String> testTime = new ArrayList<>(Arrays.asList("09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"));
        testDate.setTime(new Date(21500000)); //48960000 for 17:00, 0 for everything
        //System.out.println(testDate.getTime());
        testObject.getCalendar().setTime(testDate.getTime());
        //System.out.println(testObject.generateAvailableTime(testDate));
        assert testObject.generateAvailableTime(testDate).equals(testTime);
    }

    @Test
    void generateAvailableTimeTwelveTest() {
        List<String> testTime = new ArrayList<>(Arrays.asList("13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"));
        testDate.setTime(new Date(32400000)); //48960000 for 17:00, 0 for everything
        //System.out.println(testDate.getTime());
        testObject.getCalendar().setTime(testDate.getTime());
        //System.out.println(testObject.generateAvailableTime(testDate));
        assert testObject.generateAvailableTime(testDate).equals(testTime);
    }
}