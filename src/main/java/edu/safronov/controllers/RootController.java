package edu.safronov.controllers;

import edu.safronov.domain.CallRequest;
import edu.safronov.models.dto.CallRequestDto;
import edu.safronov.repos.UserRepository;
import edu.safronov.services.captcha.recaptcha.CaptchaService;
import edu.safronov.services.communications.telegram.CallRequestNotification;
import edu.safronov.services.scheduler.SchedulerService;
import edu.safronov.utils.CallRequestUtils;
import edu.safronov.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {
    @Autowired
    private CallRequestNotification notificationService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String showRequestForm(@RequestParam(value = "user_id", required = false) Long userId,
                                  Model model) {
        CallRequestDto callRequestDto = new CallRequestDto();
        if (userId != null) {
            if (UserUtils.getUserByUserId(userRepository, userId).isPresent()) {
                callRequestDto.setUserId(userId);
                model.addAttribute("callRequestDto", callRequestDto);
                return "request";
            }
            //Обработать состояние, когда пользователя нет
        }
        return "index";
    }

    @PostMapping("/request")
    public String callRequest(@ModelAttribute CallRequestDto callRequestDto,
                              @RequestParam("g-recaptcha-response") String captchaResponse,
                              Model model) {
        model.addAttribute("callRequestDto", callRequestDto);
        if (captchaService.checkCaptcha(captchaResponse)) {
            CallRequest callRequest = CallRequestUtils.mapFromCallRequestDto(callRequestDto);
            notificationService.notify(callRequest, "newRequest");
            schedulerService.scheduleNewRequest(callRequest);
            return "result";
        }
        return "request";
    }
}
