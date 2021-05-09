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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;

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
    public ModelAndView account(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        model.addAttribute("hostedOffers", offerService.findByHostId(user.getId()));
        model.addAttribute("favouriteOffers", favouriteService.findByUserId(user.getId()));
        modelAndView.setViewName("/visitor/account");
        return modelAndView;
    }

    @GetMapping(value = "/delete/{offerId}")
    public String deleteOffer(@PathVariable("offerId") Integer offerId) {
        Offer offer = offerService.findById(offerId);
        List<Message> messages = messageService.findByOfferId(offer.getId());
        List<Favourite> favourites = favouriteService.findByOfferId(offer.getId());
        offerService.deleteOffer(offer);
        for (Message message : messages) {
            messageService.deleteMessage(message);
        }
        for (Favourite favourite : favourites) {
            favouriteService.deleteFavourite(favourite);
        }

        return "redirect:/visitor/account";
    }
}
