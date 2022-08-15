package edu.safronov.models.dto;

import edu.safronov.domain.Time;
import lombok.Getter;
import lombok.Setter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CallRequestDto {

    @Setter
    private String name;

    @Setter
    private String phone;

    @Setter
    private String date;

    @Setter
    private String time;

    @Getter
    @Setter
    private Long userId;

    @Override
    public String toString() {
        return "CallRequestDto{" + "name='" + name + '\'' + ", phone='" + phone + '\'' + ", date='" + date + '\'' + ", time='" + time + '\'' + ", userId=" + userId + '}';
    }

    public String getDate() {
        if (this.date == null)
            return getValidDateTime(ZonedDateTime.now(ZoneId.of("Europe/Moscow")))
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return this.date;
    }

    public String getTime() {
        if (this.time == null)
            return getValidDateTime(ZonedDateTime.now(ZoneId.of("Europe/Moscow")))
                    .format(DateTimeFormatter.ofPattern("HH:mm"));
        return this.time;
    }

    public ZonedDateTime getDateTime() {
        if (this.date != null && this.time != null) {
            return ZonedDateTime.parse(this.date + " " + this.time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of("Europe/Moscow")));
        }
        return ZonedDateTime.now();
    }

    public String getName() {
        if (this.name == null) return "";
        return this.name;
    }

    public String getPhone() {
        if (this.phone == null) {
            return "";
        }
        return this.phone;
    }

    public ZonedDateTime getValidDateTime(ZonedDateTime dateTime) {
        ZonedDateTime tmpDateTime = ZonedDateTime.now();
        tmpDateTime = tmpDateTime.minusHours(tmpDateTime.getHour())
                .plusHours(9)
                .minusMinutes(tmpDateTime.getMinute())
                .minusSeconds(tmpDateTime.getSecond())
                .minusNanos(tmpDateTime.getNano());
        if (dateTime.isBefore(tmpDateTime))
            return tmpDateTime;
        if (Time.isCorrect(dateTime.getHour()))
            return dateTime;
        return tmpDateTime.plusDays(1);
    }
}
