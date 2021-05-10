package application.controller;

import application.entity.OfferSearch;
import application.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class LandingController {

    @Autowired
    private OfferService offerService;

    //TODO добавить фильтры
    @GetMapping(value = "/")
    public ModelAndView landing(Model model) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("offerSearch", new OfferSearch());
        model.addAttribute("offerDescriptions", offerService.findAll());
        modelAndView.setViewName("landing");
        return modelAndView;
    }
}