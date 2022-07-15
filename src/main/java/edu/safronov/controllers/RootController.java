package edu.safronov.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class RootController {
    @GetMapping("/")
    public String showRootView(HttpServletRequest request) {
        Optional<String> mobileHeader = Optional.ofNullable(request.getHeader("user-agent"));
        if (mobileHeader.isPresent()) {
            if (mobileHeader.get().contains("Android") || mobileHeader.get().contains("iPhone")) {
                System.out.println("Mobile client");
                return "index-mobile"; //TODO: необходимо разработать шаблон для смартфонов
            }
        }
        return "index";
    }
}
