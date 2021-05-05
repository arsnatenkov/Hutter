package application.controller;

import application.entity.Offer;
import application.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RequiredArgsConstructor
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
