package application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LandingController {
    @GetMapping("/")
    public ModelAndView landing(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("landing");
        return modelAndView;
    }
}
