package com.golfcharity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(jakarta.servlet.http.HttpServletRequest request, java.security.Principal principal) {
        if (principal != null) {
            try {
                request.logout();
            } catch (jakarta.servlet.ServletException e) {
                e.printStackTrace();
            }
        }
        return "index";
    }
    
    @GetMapping("/pricing")
    public String pricing() {
        return "pricing";
    }
}
