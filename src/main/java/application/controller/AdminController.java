package application.controller;

import application.entity.Favourite;
import application.entity.Message;
import application.entity.Offer;
import application.entity.User;
import application.converter.UserToUserDto;
import application.service.FavouriteService;
import application.service.MessageService;
import application.service.OfferService;
import application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AdminController {
    @Autowired
    private OfferService offerService;
    @Autowired
    private UserService userService;
    @Autowired
    private FavouriteService favouriteService;
    @Autowired
    private MessageService messageService;

    /**
     * Метод для перехода на страницу админа
     */
    @GetMapping(value = "/admin")
    public String account(Model model) {
//        model.addAttribute("title", "Главная страница");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.findUserByUserName(auth.getName());
        UserToUserDto utud = new UserToUserDto();
        if (!utud.convert(u).isAdmin())
            return "redirect:/visitor/account";

        //        modelAndView.addObject("offerSearch", new SearchDTO());
        model.addAttribute("offers", offerService.findAll());
        model.addAttribute("users", userService.findAll());
//        modelAndView.setViewName("/visitor/account");
        return "admin";
    }

    @GetMapping(value = "/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.findUserByUserName(auth.getName());
        UserToUserDto utud = new UserToUserDto();
        if (!utud.convert(u).isAdmin())
            return "redirect:/visitor/account";

        User user = userService.getUser(id);

        userService.deleteUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/deleteOffer/{id}")
    public String deleteOffer(@PathVariable("id") Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User u = userService.findUserByUserName(auth.getName());
        UserToUserDto utud = new UserToUserDto();
        if (!utud.convert(u).isAdmin())
            return "redirect:/visitor/account";

        Optional<Offer> offer = offerService.findById(id);
        List<Message> messages = messageService.findByOfferId(offer.get().getId());
        List<Favourite> favourites = favouriteService.findByOfferId(offer.get().getId());
        offerService.deleteOffer(offer.get());

        for (Message message : messages)
            messageService.deleteMessage(message);

        for (Favourite favourite : favourites)
            favouriteService.deleteFavourite(favourite);

        return "redirect:/admin";
    }
}
