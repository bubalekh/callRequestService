package edu.safronov.utils;

import edu.safronov.domain.CallRequest;
import edu.safronov.models.dto.CallRequestDto;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotNull;

public class CallRequestUtils {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static String getParsedPhone(String phone) {
        var temp = phone.replaceAll("\\D", "");
        return "+7" + temp.substring(temp.length() - 10);
    }

    public static CallRequest mapFromCallRequestDto(@NotNull CallRequestDto dto) {
        modelMapper.typeMap(CallRequestDto.class, CallRequest.class).addMappings(mapper -> {
            mapper.skip(CallRequest::setId);
            mapper.map(CallRequestDto::getDateTime,
                    CallRequest::setDate);
        });
        return modelMapper.map(dto, CallRequest.class);
    }
}