package application.controller;

import application.entity.Favourite;
import application.entity.Message;
import application.entity.Offer;
import application.entity.User;
import application.service.FavouriteService;
import application.service.MessageService;
import application.service.OfferService;
import application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private MessageService messageService;

    @GetMapping(value = "/visitor/account")
    public ModelAndView account() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        List<Offer> offers = offerService.findByHostId(user.getId());
        List<Favourite> favourites = favouriteService.findByUserId(user.getId());

        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (Offer offer : offers)
            sb.append("<li class=\"offer-list\">")
                    .append(offer.linkTitle("list-norm-font", "messages"))
                    .append("&nbsp;&nbsp;").append(offer.deleteBtn()).append("</li><br/>");
        sb.append("</ul>");
        modelAndView.addObject("hostedOffers", sb.toString());

        sb = new StringBuilder();
        sb.append("<ul>");
        for (Favourite favourite : favourites)
            sb.append("<li class=\"offer-list\">").append(offerService.findById(favourite.getOfferId())
                    .linkTitle("list-norm-font", "conversation"))
                    .append("</li><br/>");
        sb.append("</ul>");
        modelAndView.addObject("favouriteOffers", sb.toString());

        modelAndView.setViewName("/visitor/account");
        return modelAndView;
    }

    @PostMapping(value = "/delete/{offerId}")
    public String deleteOffer(@PathVariable("offerId") Integer offerId) {
        Offer offer = offerService.findById(offerId);
        List<Message> messages = messageService.findByOfferId(offer.getId());
        List<Favourite> favourites = favouriteService.findByOfferId(offer.getId());
        offerService.deleteOffer(offer);
        for(Message message : messages){
            messageService.deleteMessage(message);
        }
        for(Favourite favourite : favourites){
            favouriteService.deleteFavourite(favourite);
        }

        return "redirect:/visitor/account";
    }

}
