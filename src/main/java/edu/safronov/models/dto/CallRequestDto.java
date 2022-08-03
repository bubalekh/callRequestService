package edu.safronov.models.dto;

import lombok.Getter;
import lombok.Setter;

public class CallRequestDto {

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String phone;

    @Getter
    @Setter
    private String date;

    @Getter
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
}
