package edu.safronov.services.scheduler;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerServiceTest {

    @Test
    public void calculateDuration() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        ZonedDateTime nowPlusHour = now.plusHours(1);
        System.out.println(nowPlusHour.getLong(ChronoField.INSTANT_SECONDS) - now.getLong(ChronoField.INSTANT_SECONDS));
    }

}