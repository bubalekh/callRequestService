package edu.safronov.models.dto;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CallRequestDtoTest {

    @Test
    void getDateTime() {
        String time = "17:00";
        String date = "2022-08-14";

        CallRequestDto callRequestDto = new CallRequestDto();
        callRequestDto.setTime(time);
        callRequestDto.setDate(date);
        callRequestDto.setUserId(571900962L);
        callRequestDto.setPhone("+79875288748");
        callRequestDto.setName("userName");

        assertEquals(date + " " + time, callRequestDto
                .getDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.of("Europe/Moscow"))));
    }
}