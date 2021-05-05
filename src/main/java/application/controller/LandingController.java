package application.controller;

import application.entity.Offer;
import application.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LandingController {

    @Autowired
    private OfferService offerService;

    @GetMapping("/")
    public ModelAndView landing() {
        ModelAndView modelAndView = new ModelAndView();
        List<Offer> offers = offerService.findAll();
        StringBuilder sb = new StringBuilder();

        for (Offer offer : offers)
            sb.append(wrap(offer));

        modelAndView.addObject("offerDescriptions", sb.toString());
        modelAndView.setViewName("landing");
        return modelAndView;
    }

    private String wrap(Offer offer) {
        String res = "<li><table><tr>" +
                "<td class=\"offer-pic\">" +
                "<img src=\"images/offer/offer" + offer.getId() + ".jpg\" alt=\"Фото объявления\"/>" + "</td>";

        res += "<td>" + offer.linkTitle() + offer.shortDescription();
        return res + "</td></tr></table></li>";
    }
}