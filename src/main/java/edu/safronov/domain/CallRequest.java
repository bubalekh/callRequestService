package edu.safronov.domain;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
public class CallRequest implements Comparable<CallRequest> {
    @Transient
    private final ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String phone;
    @Transient
    @Setter
    private String date;

    @Setter
    private String notificationDate;
    @Transient
    @Getter
    private String time;
    @Getter
    @Setter
    private Long userId;

    @Getter
    @Setter
    private boolean active;

    public String getParsedPhone() {
        var temp = phone.replaceAll("\\D", "");
        return "+7" + temp.substring(temp.length() - 10);
    }

    public String getDate() {
        return this.dateTime.toLocalDate().toString();
    }

    public int getHours() {
        return new Time(this.time).getHours();
    }

    public int getMinutes() {
        return new Time(this.time).getMinutes();
    }

    public ZonedDateTime getDateTime() {
        return this.dateTime;
    }

    public String getNotificationDate() {
        if (this.time != null) {
            addTimeToDate(this.time);
        }
        return this.dateTime.toLocalDateTime().toString();
    }

    public void setTime(String time) {
        this.time = time;
        Time timeWrapper = new Time(time);
        var hoursAmount = timeWrapper.getHours() - this.dateTime.getHour();
        var minutesAmount = timeWrapper.getMinutes() - this.dateTime.getMinute();
        if (hoursAmount >= 0) {
            this.dateTime.plusHours(hoursAmount);
            this.dateTime.plusMinutes(minutesAmount);
        }
    }

    @Override
    public String toString() {
        return "CallRequest{" +
                "dateTime=" + dateTime +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", userId=" + userId +
                ", active=" + active +
                '}';
    }

    public void addTimeToDate(@NotNull String time) {
        if (time.length() == 5) {
            var hours = Integer.parseInt(time.substring(0, time.indexOf(':')));
            var minutes = Integer.parseInt(time.substring(time.indexOf(':') + 1));
            dateTime.plusHours(hours);
            dateTime.plusMinutes(minutes);
        }
    }

    @Override
    public int compareTo(@NotNull CallRequest o) {
        if (LocalDate.parse(o.getDate()).isAfter(this.getDateTime().toLocalDate())) {
            return -1;
        } else if (LocalDate.parse(o.getDate()).isBefore(this.getDateTime().toLocalDate())) {
            return 1;
        } else return 0;
    }
}
