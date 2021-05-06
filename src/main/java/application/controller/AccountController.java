package application.controller;

import application.entity.Favourite;
import application.entity.Offer;
import application.entity.User;
import application.service.FavouriteService;
import application.service.OfferService;
import application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private OfferService offerService;
    @Autowired
    private UserService userService;
    @Autowired
    private FavouriteService favouriteService;

    @GetMapping(value = "/visitor/account")
    public ModelAndView account() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        List<Offer> offers = offerService.findByHostId(user.getId());
        List<Offer> favouriteOffers = new ArrayList<>();
        List<Favourite> favourites = favouriteService.findByUserId(user.getId());
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();

        for (Offer offer : offers)
            sb.append("<div>").append(offer.linkTitle("list-norm-font", "messages"))
                    .append(offer.editBtn()).append("</div>").append("<br/>");
        modelAndView.addObject("hostedOffers", sb.toString());


        for(Favourite favourite : favourites)
            favouriteOffers.add(offerService.findById(favourite.getOfferId()));

        for(Offer offer : favouriteOffers)
            sb1.append("<div>").append(offer.linkTitle("list-norm-font", "conversation"))
                    .append("</div>").append("<br/>");



        modelAndView.addObject("favouriteOffers", sb1.toString());
        modelAndView.setViewName("/visitor/account");
        return modelAndView;
    }

}
