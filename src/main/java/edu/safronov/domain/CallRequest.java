package edu.safronov.domain;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
    private String date;
    @Getter
    private String time;

    @Getter
    @Setter
    private boolean active;

    public String getParsedPhone() {
        var temp = phone.replaceAll("\\D", "");
        return "+7" + temp.substring(temp.length() - 10);
    }

    public String getDate() {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void setDate(String date) throws ParseException {
        this.date = date;
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
        return "CallRequestModel{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", date=" + date +
                ", time='" + time + '\'' +
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
        if (this.getHours() > o.getHours()) {
            if (this.getMinutes() > o.getMinutes()) {
                return 1;
            } else return -1;
        } else if (this.getHours() < o.getHours())
            return -1;
        return 0;
    }
}
