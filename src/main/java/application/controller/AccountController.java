package application.controller;

import application.dto.SearchDTO;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public String deleteOffer(@PathVariable("offerId") Long offerId) {
        Optional<Offer> offer = offerService.findById(offerId);
        List<Message> messages = messageService.findByOfferId(offer.get().getId());
        List<Favourite> favourites = favouriteService.findByOfferId(offer.get().getId());
        offerService.deleteOffer(offer.get());

        for (Message message : messages)
            messageService.deleteMessage(message);

        for (Favourite favourite : favourites)
            favouriteService.deleteFavourite(favourite);

        return "redirect:/visitor/account";
    }

    @GetMapping(value = "/searchFavourite")
    public ModelAndView search(HttpServletRequest request, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        List<String> rooms = new ArrayList<>();
        rooms.add(request.getParameter("noRooms"));
        rooms.add(request.getParameter("rooms1"));
        rooms.add(request.getParameter("rooms2"));
        rooms.add(request.getParameter("rooms3"));
        rooms.add(request.getParameter("manyRooms"));
        String lowerBound = request.getParameter("lowerCostBound");
        String higherBound = request.getParameter("higherCostBound");
        final long lowerCost = lowerBound.isEmpty() ? 0L : Long.parseLong(lowerBound);
        final long higherCost = higherBound.isEmpty() ? Long.MAX_VALUE : Long.parseLong(higherBound);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        List<Favourite> favourites = favouriteService.findByUserId(user.getId());
        List<Favourite> finish = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            if (rooms.get(i) != null) {
                for(Favourite favourite : favourites){
                    if(offerService.findById(favourite.getOfferId()).get().getQuantityRoom() == Integer.parseInt(rooms.get(i))){
                        finish.add(favourite);
                    }
                }
            }
        }
        if(rooms.get(4) != null){
            for(Favourite favourite : favourites){
                if(offerService.findById(favourite.getOfferId()).get().getQuantityRoom() == Integer.parseInt(rooms.get(4))){
                    finish.add(favourite);
                }
            }
        }

        if (finish.isEmpty() && lowerCost != 0L && higherCost != Long.MAX_VALUE) {
            for(Favourite favourite : favourites){
                if(offerService.findById(favourite.getOfferId()).get().getCost() > lowerCost && offerService.findById(favourite.getOfferId()).get().getCost() < higherCost){
                    finish.add(favourite);
                }
            }
        } else {
            finish.removeIf(o -> offerService.findById(o.getOfferId()).get().getCost() < lowerCost || offerService.findById(o.getOfferId()).get().getCost() > higherCost);
        }

        if (rooms.stream().allMatch(Objects::isNull) && lowerBound.isEmpty() && higherBound.isEmpty()) {
            finish.addAll(favourites);
        }

        model.addAttribute("favouriteOffers", finish);
        modelAndView.addObject("offerSearch", new SearchDTO());
        modelAndView.setViewName("landing");
        return modelAndView;
    }
}
