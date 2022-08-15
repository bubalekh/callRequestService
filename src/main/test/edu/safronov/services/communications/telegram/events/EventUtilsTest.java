package edu.safronov.services.communications.telegram.events;

import edu.safronov.utils.EventUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventUtilsTest {

    @Test
    void getActionParameter() {
        String testString = "/list abc";
        assertEquals("abc", EventUtils.getActionParameter(testString).get());
        testString = "/list";
        assertTrue(EventUtils.getActionParameter(testString).isEmpty());
        testString = "/list aba abvaba ababa";
        assertEquals("aba abvaba ababa", EventUtils.getActionParameter(testString).get());
    }
}