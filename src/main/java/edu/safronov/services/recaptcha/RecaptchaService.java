package edu.safronov.services.recaptcha;

import edu.safronov.models.dto.RecaptchaResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@ConfigurationProperties(prefix = "google.recaptcha.key")
public class RecaptchaService implements CaptchaService {
    @Getter
    @Setter
    private String site;
    @Getter
    @Setter
    private String secret;

    @Override
    public boolean checkCaptcha(@NotNull String captchaResponse) {
        RestTemplate template = new RestTemplate();
        String url = "https://google.com/recaptcha/api/siteverify?secret=%s&response=%s";
        RecaptchaResponseDto response = template.postForObject(String.format(url, secret, captchaResponse), Collections.emptyList(), RecaptchaResponseDto.class);
        return response != null && response.isSuccess();
    }
}
