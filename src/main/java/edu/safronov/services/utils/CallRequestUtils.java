package edu.safronov.services.utils;

import edu.safronov.domain.CallRequest;
import edu.safronov.models.dto.CallRequestDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CallRequestUtils {
    public static String getParsedPhone(String phone) {
        var temp = phone.replaceAll("\\D", "");
        return "+7" + temp.substring(temp.length() - 10);
    }

    public static void cloneValues(CallRequestDto dto, CallRequest entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        entity.setUserId(dto.getUserId());
        if (dto.getTime() == null)
            dto.setTime(ZonedDateTime.now(ZoneId.of("Europe/Moscow")).format(DateTimeFormatter.ofPattern("HH:mm")));
        if (dto.getDate() == null)
            dto.setDate(ZonedDateTime.now(ZoneId.of("Europe/Moscow")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        String dateAndTimeToParse = dto.getDate() + " " + dto.getTime();
        entity.setDate(LocalDateTime.parse(dateAndTimeToParse, formatter));
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
    }
}