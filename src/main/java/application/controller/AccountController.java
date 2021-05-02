package application.controller;

import application.entity.Offer;
import application.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

public class AccountController {

    @Autowired
    private OfferService offerService;

    @GetMapping("/visitor/account")
    public ModelAndView account() {
        ModelAndView modelAndView = new ModelAndView();
        List<Offer> offers = offerService.findAll();
        StringBuilder sb = new StringBuilder();

        for (Offer offer : offers)
            sb.append(offer.getAddress()).append("<br/>");

        modelAndView.addObject("hostedOffers", sb.toString());
        modelAndView.setViewName("/visitor/account");
        return modelAndView;
    }

}
