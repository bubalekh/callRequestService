package edu.safronov.services.utils;

import edu.safronov.domain.CallRequest;
import edu.safronov.models.dto.CallRequestDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CallRequestUtilsTest {

    @Test
    void mapFromCallRequestDto() {
        CallRequestDto callRequestDto = new CallRequestDto();
        callRequestDto.setTime("17:00");
        callRequestDto.setDate("2022-08-14");
        callRequestDto.setUserId(571900962L);
        callRequestDto.setPhone("+79875288748");
        callRequestDto.setName("userName");

        CallRequest callRequest = CallRequestUtils.mapFromCallRequestDto(callRequestDto);

        assertEquals(callRequestDto.getName(), callRequest.getName());
        assertEquals(callRequestDto.getPhone(), callRequest.getPhone());
        assertEquals(callRequestDto.getUserId(), callRequest.getUserId());
        assertNotEquals(callRequestDto.getUserId(), callRequest.getId());
        assertEquals(callRequestDto.getDateTime(), callRequest.getDate());
    }
}