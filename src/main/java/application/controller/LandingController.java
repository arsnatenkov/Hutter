package application.controller;

import application.entity.Offer;
import application.service.OfferService;
import application.service.UserService;
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

    @Autowired
    private UserService userService;

    //TODO добавить фильтры
    @GetMapping(value = "/")
    public ModelAndView landing(Model model) {
        ModelAndView modelAndView = new ModelAndView();
//        List<Offer> offers = offerService.findAll();
//        StringBuilder sb = new StringBuilder();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findUserByUserName(auth.getName());
//        for (Offer offer : offers) {
//            if (user != null) {
//                sb.append(wrap(offer, offer.getHostId().equals(user.getId()) ? "messages" : "conversation"));
//            } else {
//                sb.append(wrap(offer, "offer"));
//            }
//        }
//        modelAndView.addObject("offerDescriptions", sb.toString());

        model.addAttribute("offerDescriptions", offerService.findAll());
        modelAndView.setViewName("landing");
        return modelAndView;
    }

    private String wrap(Offer offer, String map) {
        String res = "<li><table><tr>" +
                "<td class=\"offer-pic\"><div>" +
                "<img src=\"/images/offer/offer" + offer.getId() +
                ".jpg\" alt=\"Фото объявления\"/></div></td>";

        res += "<td>" + offer.linkTitle("", map) + "<br/>" + offer.shortDescription(map);
        return res + "</td></tr></table></li>";
    }
}