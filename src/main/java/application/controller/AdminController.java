package application.controller;

import application.dto.UserDTO;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

    private class Msg {
        public Message message;
        public String sender;
    }

    private class Usr{
        public User user;
        public String name;
        public String lastName;
    }

    /**
     * Метод для перехода на страницу админа
     */
    @GetMapping(value = "/admin")
    public String admin(Model model) {
//        model.addAttribute("title", "Главная страница");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean a = userService.findUserByUserName(auth.getName()) != null;

//        User u = userService.findUserByUserName(auth.getName());
//        UserToUserDto utud = new UserToUserDto();
//        if (!utud.convert(u).isAdmin())
//            return "redirect:/visitor/account";

        //        modelAndView.addObject("offerSearch", new SearchDTO());
        model.addAttribute("offers", offerService.findAll());
        List<User> users = new ArrayList<>();
        List<User> banned = new ArrayList<>();
        for (User u : userService.findAll()) {
//            Usr usr = new Usr();
//            usr.user = u;
//            usr.name = new UserToUserDto().convert(u).getName();
//            usr.lastName = new UserToUserDto().convert(u).getLastName();

            if (u.getActive()) {
                users.add(u);
            } else {
                banned.add(u);
            }
        }

        model.addAttribute("users", users);
        model.addAttribute("banned", banned.isEmpty() ? null : banned);

        List<Msg> msgs = new ArrayList<>();
        List<Message> messages = messageService.findAll();
        for (Message m : messages) {
            Msg i = new Msg();
            i.message = m;
            i.sender = "[" + m.getSender().getId().toString() + "] " + m.getSender().getName();
            msgs.add(i);
        }

//        model.addAttribute("messages", messages);
//        model.addAttribute("senders", senders);

        model.addAttribute("messages", msgs);

//        modelAndView.setViewName("/visitor/account");
        return "admin";
    }

    @GetMapping(value = "/banUser/{id}")
    public String banUser(@PathVariable("id") Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean a = userService.findUserByUserName(auth.getName()) != null;

//        User u = userService.findUserByUserName(auth.getName());
//        UserToUserDto utud = new UserToUserDto();
//        if (!utud.convert(u).isAdmin())
//            return "redirect:/visitor/account";

        User user = userService.getUser(id);
//        userService.deleteUser(user);
        userService.banUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/unbanUser/{id}")
    public String unbanUser(@PathVariable("id") Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean a = userService.findUserByUserName(auth.getName()) != null;

//        User u = userService.findUserByUserName(auth.getName());
//        UserToUserDto utud = new UserToUserDto();
//        if (!utud.convert(u).isAdmin())
//            return "redirect:/visitor/account";

        User user = userService.getUser(id);
//        userService.deleteUser(user);
        userService.unbanUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/deleteOffer/{id}")
    public String deleteOffer(@PathVariable("id") Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean a = userService.findUserByUserName(auth.getName()) != null;

//        User u = userService.findUserByUserName(auth.getName());
//        UserToUserDto utud = new UserToUserDto();
//        if (!utud.convert(u).isAdmin())
//            return "redirect:/visitor/account";

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

    @GetMapping(value = "/deleteMessage/{id}")
    public String deleteMessage(@PathVariable("id") Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean a = userService.findUserByUserName(auth.getName()) != null;

//        User u = userService.findUserByUserName(auth.getName());
//        UserToUserDto utud = new UserToUserDto();
//        if (!utud.convert(u).isAdmin())
//            return "redirect:/visitor/account";

        Optional<Message> message = messageService.findById(id);
        messageService.deleteMessage(message.get());
        return "redirect:/admin";
    }

//    /**
//     * Метод для перехода на страницу пользователя
//     */
//    @GetMapping(value = "/user")
//    public ModelAndView user(HttpServletRequest request, Model model) {
//        String id = request.getParameter("id");
//        User user = userService.getUser(Long.parseLong(id));
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        model.addAttribute("isAuth", userService.findUserByUserName(auth.getName()) != null);
//
//        model.addAttribute("user", user);
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("user");
//        return modelAndView;
//    }

}
