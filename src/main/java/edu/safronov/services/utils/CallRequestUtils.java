package edu.safronov.services.utils;

import edu.safronov.domain.CallRequest;
import edu.safronov.models.dto.CallRequestDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CallRequestUtils {
    public static String getParsedPhone(String phone) {
        var temp = phone.replaceAll("\\D", "");
        return "+7" + temp.substring(temp.length() - 10);
    }

    private static void addTimeToDate(@NotNull String time, LocalDateTime dateTime) {
        if (time.length() == 5) {
            var hours = Integer.parseInt(time.substring(0, time.indexOf(':')));
            var minutes = Integer.parseInt(time.substring(time.indexOf(':') + 1));
            dateTime = dateTime.plusHours(hours).plusMinutes(minutes);
        }
    }

    public static void cloneValues(CallRequestDto dto, CallRequest entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        entity.setUserId(dto.getUserId());
        String dateAndTimeToParse = dto.getDate() + " " + dto.getTime();
        entity.setDate(LocalDateTime.parse(dateAndTimeToParse, formatter));
        //addTimeToDate(dto.getTime(), entity.getDate());
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
    }
}