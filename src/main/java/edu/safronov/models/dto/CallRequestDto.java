package edu.safronov.models.dto;

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

    @Getter
    @Setter
    private boolean active;

    @Override
    public String toString() {
        return "CallRequestDto{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", userId=" + userId +
                ", active=" + active +
                '}';
    }

    public String getDate(){
        if (this.date == null)
            return ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return this.date;
    }

    public String getTime() {
        if (this.time == null)
            return ZonedDateTime.now(ZoneId.of("Europe/Moscow"))
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm"));
        return this.time;
    }

    public String getName() {
        if (this.name == null)
            return "";
        return this.name;
    }

    public String getPhone() {
        if (this.phone == null) {
            return "";
        }
        return this.phone;
    }
}
