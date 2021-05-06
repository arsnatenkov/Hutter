package application.controller;

import application.entity.Offer;
import application.entity.User;
import application.service.OfferService;
import application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LandingController {

    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public ModelAndView landing() {
        ModelAndView modelAndView = new ModelAndView();
        List<Offer> offers = offerService.findAll();
        StringBuilder sb = new StringBuilder();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        for (Offer offer : offers)
            if(user != null){
                if(offer.getHostId() == user.getId()){
                    sb.append(wrap(offer, "messages"));
                }else{
                    sb.append(wrap(offer, "conversation"));
                }
            }else{
                sb.append(wrap(offer, "offer"));
            }



        modelAndView.addObject("offerDescriptions", sb.toString());
        modelAndView.setViewName("landing");
        return modelAndView;
    }

    private String wrap(Offer offer, String map) {
        String res = "<li><table><tr>" +
                "<td class=\"offer-pic\">" +
                "<img src=\"images/offer/offer" + offer.getId() + ".jpg\" alt=\"Фото объявления\"/>" + "</td>";

        res += "<td>" + offer.linkTitle("") + offer.shortDescription(map);
        return res + "</td></tr></table></li>";
    }
}