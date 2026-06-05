package com.example.thisaraprinters.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard(Authentication authentication) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("dashboard");
        if (authentication != null) {
            mav.addObject("username", authentication.getName());
            mav.addObject("authorities", authentication.getAuthorities());
        }
        return mav;
    }
}
