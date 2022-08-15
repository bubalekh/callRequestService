package edu.safronov.models.dto;

import edu.safronov.domain.Time;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

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

    @Test
    void getValidDateTime() {
        String timeFormRequest = "16:00";
        String dateFromRequest = "2018-11-03";

        CallRequestDto callRequestDto = new CallRequestDto();
        callRequestDto.setTime(timeFormRequest);
        callRequestDto.setDate(dateFromRequest);

        System.out.println(callRequestDto.getDateTime());

        ZonedDateTime zonedDateTimeExpired = ZonedDateTime.of(LocalDateTime.parse("2018-11-03T19:45:30"), ZoneId.of("Europe/Moscow"));
        ZonedDateTime tmpDateTime;

        try (MockedStatic<ZonedDateTime> mockedStatic = Mockito.mockStatic(ZonedDateTime.class)) {
            mockedStatic.when(ZonedDateTime::now).thenReturn(zonedDateTimeExpired);
            tmpDateTime = ZonedDateTime.now();
            mockedStatic.verify(ZonedDateTime::now);
        }
        tmpDateTime = tmpDateTime.minusHours(tmpDateTime.getHour())
                .plusHours(9)
                .minusMinutes(tmpDateTime.getMinute())
                .minusSeconds(tmpDateTime.getSecond())
                .minusNanos(tmpDateTime.getNano());

        if (callRequestDto.getDateTime().isBefore(tmpDateTime)) {
            System.out.println(tmpDateTime); //Если запрос сделан на более ранюю дату и время, чем это допустимо, то вернуть ближайшую нормальную дату
        }
        if (Time.isCorrect(timeFormRequest))
            System.out.println(callRequestDto.getDateTime()); //Если вермя норм - то вернуть дату из запроса
        System.out.println(tmpDateTime.plusDays(1));
    }
}