package application.controller;

import application.entity.OfferSearch;
import application.entity.User;
import application.service.OfferSearchService;
import application.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class LandingController {

    @Autowired
    private OfferService offerService;
    @Autowired
    private OfferSearchService offerSearchService;

    //TODO добавить фильтры
    @GetMapping(value = "/")
    public ModelAndView landing(Model model) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("offerSearch", new OfferSearch());
        model.addAttribute("offerDescriptions", offerService.findAll());
        modelAndView.setViewName("landing");
        return modelAndView;
    }

    @GetMapping(value = "/search")
    public ModelAndView registration(Model model) { // like common landing
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("offerSearch", new OfferSearch());
        model.addAttribute("offerDescriptions", offerService.findAll());
        modelAndView.setViewName("landing");
        return modelAndView;
    }

    @PostMapping(value = "/search")
    public ModelAndView createNewUser(@Valid OfferSearch offerSearch) {
        ModelAndView modelAndView = new ModelAndView();
        offerSearchService.saveOfferSearch(new OfferSearch("sasdasdas",
                Long.parseLong("0"), Long.parseLong("10000")));
//        offerSearchService.saveOfferSearch(offerSearch);
        modelAndView.setViewName("registration");
        return modelAndView;
    }
}