package edu.safronov.controllers;

import edu.safronov.domain.CallRequest;
import edu.safronov.repos.CallRequestRepository;
import edu.safronov.services.recaptcha.RecaptchaService;
import edu.safronov.services.scheduler.SchedulerService;
import edu.safronov.services.telegram.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class RootController {

    @Autowired
    private TelegramService telegramService;
    @Autowired
    private RecaptchaService recaptchaService;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private CallRequestRepository callRequestRepository;
    private String templateType = "desktop/"; //По умолчанию отдаем шаблон для десктопов
    @GetMapping("/")
    public String showRootView(@RequestHeader("User-Agent") String agent,
                               Model model)
    {
        model.addAttribute("callRequest", new CallRequest());
        Optional<String> mobileHeader = Optional.ofNullable(agent);
        if (mobileHeader.isPresent()) {
            if (mobileHeader.get().contains("Android") || mobileHeader.get().contains("iPhone")) {
                //templateType = "mobile/"; //TODO: необходимо разработать шаблон для смартфонов
            }
        }
        return templateType + "index";
    }

    @PostMapping("/request")
    public String callRequest(@ModelAttribute CallRequest callRequest,
                              @RequestParam("g-recaptcha-response") String recaptchaResponse,
                              Model model)
    {
        callRequest.addTimeToDate(callRequest.getTime());
        model.addAttribute("callRequest", callRequest);
        if (recaptchaService.isHuman(recaptchaResponse)) {
            callRequest.setActive(true);
            callRequestRepository.save(callRequest);
            telegramService.callRequestNotification(callRequest, true);
            schedulerService.checkNewRequest(callRequest);
            return templateType + "result";
        }
        return templateType + "index";
    }
}
