package application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

public class AccountController {
    @GetMapping("/account")
    public ModelAndView landing(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("account");
        return modelAndView;
    }
}
