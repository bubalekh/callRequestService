package edu.safronov.controllers;

import edu.safronov.models.CallRequestModel;
import edu.safronov.services.RecaptchaService;
import edu.safronov.services.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class RootController {

    @Autowired
    private TelegramService telegramService;
    @Autowired
    private RecaptchaService recaptchaService;
    private String templateType = "desktop/"; //По умолчанию отдаем шаблон для десктопов
    @GetMapping("/")
    public String showRootView(HttpServletRequest request,
                               Model model)
    {
        model.addAttribute("callRequest", new CallRequestModel());
        Optional<String> mobileHeader = Optional.ofNullable(request.getHeader("user-agent"));
        if (mobileHeader.isPresent()) {
            if (mobileHeader.get().contains("Android") || mobileHeader.get().contains("iPhone")) {
                //templateType = "mobile/"; //TODO: необходимо разработать шаблон для смартфонов
            }
        }
        return templateType + "index";
    }

    @PostMapping("/request")
    public String callRequest(@ModelAttribute CallRequestModel callRequest,
                              @RequestParam("g-recaptcha-response") String recaptchaResponse,
                              Model model)
    {
        callRequest.addTimeToDate(callRequest.getTime());
        model.addAttribute("callRequest", callRequest);
        if (recaptchaService.isHuman(recaptchaResponse)) {
            telegramService.callRequestNotification(callRequest);
            return templateType + "result";
        }
        return templateType + "index";
    }
}
