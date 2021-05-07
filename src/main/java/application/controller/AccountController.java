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
public class AccountController {

    @Autowired
    private OfferService offerService;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/visitor/account")
    public ModelAndView account() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        List<Offer> offers = offerService.findByHostId(user.getId());
        StringBuilder sb = new StringBuilder();

        for (Offer offer : offers)
            sb.append("<div>").append(offer.linkTitle("list-norm-font"))
                    .append(offer.editBtn()).append("</div>").append("<br/>");

        modelAndView.addObject("hostedOffers", sb.toString());
        modelAndView.setViewName("/visitor/account");
        return modelAndView;
    }

}
