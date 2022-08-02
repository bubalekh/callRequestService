package edu.safronov.services.captcha.recaptcha;

public interface CaptchaService {
    boolean checkCaptcha(String captchaResponse);
}
