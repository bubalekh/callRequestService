package edu.safronov.services.recaptcha;

public interface CaptchaService {
    boolean checkCaptcha(String captchaResponse);
}
