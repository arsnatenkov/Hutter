package application.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OfferController {
    @GetMapping(value = "/offer")
    public ModelAndView offer() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("offer");

        return modelAndView;
    }
}
