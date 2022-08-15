package edu.safronov.domain;

import lombok.Getter;
import lombok.Setter;

public class Time {
    @Getter
    @Setter
    private Integer hours;
    @Getter
    @Setter
    private Integer minutes;

    public Time(String time) {
        if (time.length() == 5) {
            hours = Integer.parseInt(time.substring(0, time.indexOf(':')));
            minutes = Integer.parseInt(time.substring(time.indexOf(':') + 1));
        }
    }

    public Time(Integer hours) {
        this.hours = hours;
        this.minutes = 0;
    }

    public static boolean isCorrect(Time time) {
        return time.getHours() >= 9 && time.getHours() <= 19;
    }

    public static boolean isCorrect(String time) {
        Time timeWrapper = new Time(time);
        return timeWrapper.getHours() >= 9 && timeWrapper.getHours() <= 19;
    }

    public static boolean isCorrect(Integer time) {
        Time timeWrapper = new Time(time);
        return timeWrapper.getHours() >= 9 && timeWrapper.getHours() <= 19;
    }
}
