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

//    @PostMapping(value = "/visitor/account")
//    public ModelAndView addOffer(@Valid Offer offer, BindingResult bindingResult) {
//        ModelAndView modelAndView = new ModelAndView();
//        List<Offer> offerExists = offerService.findByAddress(offer.getAddress());
//
//        if (offerExists != null && offerExists.contains(offer)) {
//            bindingResult
//                    .rejectValue("address", "error.offer",
//                            "There is already an offer registered with same offer address provided");
//        }
//        if (!bindingResult.hasErrors()) {
//            offerService.saveOffer(offer);
//            modelAndView.addObject("successMessage", "Offer successfully added");
//            modelAndView.addObject("offer", new Offer());
//        }
//
//        modelAndView.setViewName("/visitor/account");
//        return modelAndView;
//    }
}
