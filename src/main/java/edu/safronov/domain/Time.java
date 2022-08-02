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
}
