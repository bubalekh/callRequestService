package edu.safronov.services;

import edu.safronov.models.dto.RecaptchaResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
public class RecaptchaService {
    @Getter
    @Setter
    private String site;
    @Getter
    @Setter
    private String secret;

    public boolean isHuman(@NotNull String captchaResponse) {
        RestTemplate template = new RestTemplate();
        String url = "https://google.com/recaptcha/api/siteverify?secret=%s&response=%s";
        RecaptchaResponseDto response = template.postForObject(String.format(url, secret, captchaResponse), Collections.emptyList(), RecaptchaResponseDto.class);
        System.out.println(response);
        return response != null && response.isSuccess();
    }
}
