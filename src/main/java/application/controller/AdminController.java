package application.controller;

import application.dto.MSearchDTO;
import application.dto.SearchDTO;
import application.dto.USearchDTO;
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
import groovy.lang.Tuple2;
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
import java.util.Objects;
import java.util.Optional;

import static java.lang.Math.min;

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
    public ModelAndView admin(Model model) {
//        model.addAttribute("title", "Главная страница");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/admin");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean a = userService.findUserByUserName(auth.getName()) != null;

//        User u = userService.findUserByUserName(auth.getName());
//        UserToUserDto utud = new UserToUserDto();
//        if (!utud.convert(u).isAdmin())
//            return "redirect:/visitor/account";

        //        modelAndView.addObject("offerSearch", new SearchDTO());
        modelAndView.addObject("offerSearch", new SearchDTO());
        modelAndView.addObject("userSearch", new USearchDTO());
        modelAndView.addObject("messageSearch", new MSearchDTO());
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
        return modelAndView;
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

    /**
     * Метод поиска объявлений
     * @param request Параметры запроса
     * @param model Модель страницы
     * @return Модель страницы
     */
    @GetMapping(value = "/offerSearch")
    public ModelAndView offerSearch(HttpServletRequest request, Model model, UserDTO userDTO) {
        ModelAndView modelAndView = new ModelAndView();
        List<Offer> offers = new ArrayList<>();
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

        for (int i = 0; i < 4; ++i) {
            if (rooms.get(i) != null) {
                offers.addAll(offerService.findByQuantityRoom(Integer.parseInt(rooms.get(i))));
            }
        }
        if(rooms.get(4) != null){
            offers.addAll(offerService.findByQuantityRoomMoreFour());
        }



        if (offers.isEmpty() && lowerCost != 0L && higherCost != Long.MAX_VALUE) {
            offers.addAll(offerService.findByCostBetween(lowerCost, higherCost));
        } else {
            offers.removeIf(o -> o.getCost() < lowerCost || o.getCost() > higherCost);
        }

        if (rooms.stream().allMatch(Objects::isNull) && lowerBound.isEmpty() && higherBound.isEmpty()) {
            offers.addAll(offerService.findAll());
        }

        model.addAttribute("offers", offers);
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

        modelAndView.addObject("offerSearch", new SearchDTO());
        modelAndView.addObject("userSearch", new SearchDTO());
        modelAndView.addObject("messageSearch", new SearchDTO());
        modelAndView.setViewName("/admin");
        return modelAndView;
    }
//
//    @GetMapping(value = "/userSearch")
//    public ModelAndView userSearch(HttpServletRequest request, Model model, UserDTO userDTO) {
//        ModelAndView modelAndView = new ModelAndView();
//        List<User> users = userService.findAll();
//        List<String> rooms = new ArrayList<>();
//        rooms.add(request.getParameter("active"));
//        String email = request.getParameter("email");
//        String userName = request.getParameter("userName");
//        String name = request.getParameter("name");
//        String lastName = request.getParameter("lastName");
//        final long lowerCost = lowerBound.isEmpty() ? 0L : Long.parseLong(lowerBound);
//        final long higherCost = higherBound.isEmpty() ? Long.MAX_VALUE : Long.parseLong(higherBound);
//        List<User> us = new ArrayList<>();
//        for (User u : users){
//            if (u.getIsAdmin() == rooms[0])
//        }
//        Tuple2<List<Offer>, List<Integer>> tuple = preprocess(offers, 0, userDTO);
//        model.addAttribute("offersTab", tuple.getFirst());
//        model.addAttribute("tabsLen", tuple.getSecond());
//        model.addAttribute("adminUser", userDTO.isAdmin());
//        modelAndView.addObject("offerSearch", new SearchDTO());
//        modelAndView.addObject("userSearch", new SearchDTO());
//        modelAndView.addObject("messageSearch", new SearchDTO());
//        modelAndView.setViewName("/admin");
//        return modelAndView;
//    }
//
//    @GetMapping(value = "/messageSearch")
//    public ModelAndView messageSearch(HttpServletRequest request, Model model, UserDTO userDTO) {
//        ModelAndView modelAndView = new ModelAndView();
//        List<Offer> offers = new ArrayList<>();
//        List<String> rooms = new ArrayList<>();
//        rooms.add(request.getParameter("noRooms"));
//        rooms.add(request.getParameter("rooms1"));
//        rooms.add(request.getParameter("rooms2"));
//        rooms.add(request.getParameter("rooms3"));
//        rooms.add(request.getParameter("manyRooms"));
//
//        String lowerBound = request.getParameter("lowerCostBound");
//        String higherBound = request.getParameter("higherCostBound");
//        final long lowerCost = lowerBound.isEmpty() ? 0L : Long.parseLong(lowerBound);
//        final long higherCost = higherBound.isEmpty() ? Long.MAX_VALUE : Long.parseLong(higherBound);
//
//        for (int i = 0; i < 4; ++i) {
//            if (rooms.get(i) != null) {
//                offers.addAll(offerService.findByQuantityRoom(Integer.parseInt(rooms.get(i))));
//            }
//        }
//        if(rooms.get(4) != null){
//            offers.addAll(offerService.findByQuantityRoomMoreFour());
//        }
//
//
//
//        if (offers.isEmpty() && lowerCost != 0L && higherCost != Long.MAX_VALUE) {
//            offers.addAll(offerService.findByCostBetween(lowerCost, higherCost));
//        } else {
//            offers.removeIf(o -> o.getCost() < lowerCost || o.getCost() > higherCost);
//        }
//
//        if (rooms.stream().allMatch(Objects::isNull) && lowerBound.isEmpty() && higherBound.isEmpty()) {
//            offers.addAll(offerService.findAll());
//        }
//
//        Tuple2<List<Offer>, List<Integer>> tuple = preprocess(offers, 0, userDTO);
//        model.addAttribute("offersTab", tuple.getFirst());
//        model.addAttribute("tabsLen", tuple.getSecond());
//        model.addAttribute("adminUser", userDTO.isAdmin());
//        modelAndView.addObject("offerSearch", new SearchDTO());
//        modelAndView.addObject("userSearch", new SearchDTO());
//        modelAndView.addObject("messageSearch", new SearchDTO());
//        modelAndView.setViewName("/admin");
//        return modelAndView;
//    }
}
