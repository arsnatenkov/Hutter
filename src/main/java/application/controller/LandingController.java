package application.controller;

import application.entity.Offer;
import application.entity.User;
import application.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class LandingController {

    @Autowired
    private OfferService offerService;

    @GetMapping("/")
    public ModelAndView landing(){
        ModelAndView modelAndView = new ModelAndView();
        Offer offer = offerService.getOffersByPublicId();
        String result = offer.getPublicId() + ":"
                + offer.getTotal() + ":"
                + offer.getLiving() + ":"
                + offer.getRoomArea() + ":"
                + offer.getQuantityRoom() + ":"
                + offer.getCost() + ":"
                + offer.getQuantityToilet() + ":"
                + offer.getType() + ":"
                + offer.getMaterial() + ":"
                + offer.getAddress() + ":"
                + offer.getCoordinateX() + ":"
                + offer.getCoordinateY() + ":"
                + offer.getHostId() + ":"
                + offer.getDescription() + ";";

        modelAndView.addObject("offerDescription1", result);
        modelAndView.setViewName("landing");
        return modelAndView;
    }
}
