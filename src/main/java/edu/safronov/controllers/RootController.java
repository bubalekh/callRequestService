package edu.safronov.controllers;

import edu.safronov.domain.CallRequest;
import edu.safronov.repos.CallRequestRepository;
import edu.safronov.services.captcha.recaptcha.CaptchaService;
import edu.safronov.services.communications.telegram.CallRequestNotification;
import edu.safronov.services.scheduler.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class RootController {

    @Autowired
    private CallRequestNotification notificationService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private CallRequestRepository callRequestRepository;
    private String templateType = "desktop/"; //По умолчанию отдаем шаблон для десктопов

    @GetMapping("/")
    public String showRequestForm(@RequestHeader("User-Agent") String agent,
                               @RequestParam(value = "user_id", required = false) Long userId,
                               Model model)
    {
        CallRequest callRequest = new CallRequest();
        Optional<String> mobileHeader = Optional.ofNullable(agent);
        if (mobileHeader.isPresent()) {
            if (mobileHeader.get().contains("Android") || mobileHeader.get().contains("iPhone")) {
                //templateType = "mobile/"; //TODO: необходимо разработать шаблон для смартфонов
            }
        }
        if (userId != null) {
            callRequest.setUserId(userId);
            model.addAttribute("callRequest", callRequest);
            return templateType + "request";
        }
        else {
            return templateType + "index";
        }
    }

    @PostMapping("/request")
    public String callRequest(@ModelAttribute CallRequest callRequest,
                              @RequestParam("g-recaptcha-response") String captchaResponse,
                              Model model)
    {
        callRequest.addTimeToDate(callRequest.getTime());
        model.addAttribute("callRequest", callRequest);
        if (captchaService.checkCaptcha(captchaResponse)) {
            callRequest.setActive(true);
            callRequestRepository.save(callRequest);
            notificationService.notify(callRequest, "newRequest");
            schedulerService.checkNewRequest(callRequest);
            return templateType + "result";
        }
        return templateType + "request";
    }
}
