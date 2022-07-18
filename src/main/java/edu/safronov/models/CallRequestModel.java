package edu.safronov.models;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CallRequestModel {
    private String name;
    private String phone;
    private final Calendar date = Calendar.getInstance();
    private String time;
    private final List<String> availableTime;

    public CallRequestModel() {
        this.availableTime = this.generateAvailableTime(this.date);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public String getParsedPhone() {
        var temp = phone.replaceAll("\\D", "");
        return "+7" + temp.substring(temp.length() - 10);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date.getTime());
    }

    public void setDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.date.setTime(dateFormat.parse(date));
    }

    public Calendar getCalendar() {
        return this.date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getAvailableTime() {
        return availableTime;
    }

    @Override
    public String toString() {
        return "CallRequestModel{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", date=" + date.getTime() +
                ", time='" + time + '\'' +
                '}';
    }

    public void addTimeToDate(@NotNull String time) {
        if (time.length() == 5) {
            var hours = Integer.parseInt(time.substring(0, time.indexOf(':')));
            var minutes = Integer.parseInt(time.substring(time.indexOf(':') + 1));
            date.set(Calendar.HOUR_OF_DAY, hours);
            date.set(Calendar.MINUTE, minutes);
        }
    }

    public List<String> generateAvailableTime(Calendar calendar) {
        var availableHours = calendar.get(Calendar.HOUR_OF_DAY);
        if (availableHours >= 17) {
            availableHours = 9;
            this.date.add(Calendar.DAY_OF_WEEK, 1);
            this.date.set(Calendar.MINUTE, 0);
        } else if (availableHours <= 9) {
            availableHours = 9;
            //this.date.set(Calendar.MINUTE, 0);
        }
        List<String> availableTimeList = new ArrayList<>();
        if (availableHours < 10) {
            availableTimeList.add("0" + availableHours + ":00");
            availableTimeList.add("0" + availableHours + ":30");
        }

        for (int i = availableHours + 1; i <= 16; i++) {
                availableTimeList.add(i + ":00");
                availableTimeList.add(i + ":30");
        }

        availableTimeList.add("17:00");
        availableTimeList.remove("12:00");
        availableTimeList.remove("12:30");

        return availableTimeList;
    }
}
