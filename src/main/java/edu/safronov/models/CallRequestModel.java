package edu.safronov.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CallRequestModel {
    private String name;
    private String phone;
    private Calendar date;
    private String time;

    public CallRequestModel() {
        date = Calendar.getInstance();
        date.setTime(new Date());
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public void addTimeToDate(String time) {

    }
}
