package edu.safronov.models.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecaptchaResponseDto {
    @Getter
    @Setter
    private boolean success;

    @Getter
    @Setter
    @JsonAlias("error-codes")
    private Set<String> errors;

    @Override
    public String toString() {
        return "RecaptchaResponseDto{" +
                "success=" + success +
                ", errors=" + errors +
                '}';
    }
}
