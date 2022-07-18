package edu.safronov.controllers;

import edu.safronov.models.CallRequestModel;
import edu.safronov.services.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class RootController {

    @Autowired
    private TelegramService service;
    private String templateType = "desktop/"; //По умолчанию отдаем шаблон для десктопов
    @GetMapping("/")
    public String showRootView(HttpServletRequest request, Model model) {
        model.addAttribute("callRequest", new CallRequestModel());
        Optional<String> mobileHeader = Optional.ofNullable(request.getHeader("user-agent"));
        if (mobileHeader.isPresent()) {
            if (mobileHeader.get().contains("Android") || mobileHeader.get().contains("iPhone")) {
                //templateType = "mobile/"; //TODO: необходимо разработать шаблон для смартфонов
            }
        }
        return templateType + "index";
    }

    @PostMapping("/callRequest")
    public String callRequest(@ModelAttribute CallRequestModel callRequest, Model model) {
        callRequest.addTimeToDate(callRequest.getTime());
        //System.out.println(callRequest.getPhone());
        service.callRequestNotification(callRequest);
        model.addAttribute("callRequest", callRequest);
        return templateType + "result";
    }
}
