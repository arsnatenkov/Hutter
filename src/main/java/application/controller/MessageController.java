package application.controller;

import application.converter.UserDtoToUser;
import application.converter.UserToUserDto;
import application.dto.MessageDTO;
import application.dto.UserDTO;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Controller
@RequiredArgsConstructor

public class MessageController {
    @Autowired
    private OfferService offerService;
    @Autowired
    private MessageService messagesService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDtoToUser userDtoToUser;
    @Autowired
    private UserToUserDto userToUserDto;
    @Autowired
    FavouriteService favouriteService;

    private void addConversationToModel(Long hostId, Model model, Offer offer) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        UserDTO userDTO = userToUserDto.convert(user);

        if (messagesService.findConversation(userDTO.getId(), hostId, offer.getId()) == null) {
            messagesService.saveMessage(
                    new Message(user, userDtoToUser.convert(userService.getUserById(hostId))));
        }

        model.addAttribute("messages",
                messagesService.findConversation(user.getId(), hostId, offer.getId()));
        model.addAttribute("companion", userService.getUserById(hostId));
        model.addAttribute("offer", offer);
    }

    @GetMapping(value = "/messages")
    public String getMessages(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        UserDTO userDTO = userToUserDto.convert(user);

        if (messagesService.findAllRecentMessages(userDTO.getId()) == null)
            messagesService.saveMessage(new Message());

        model.addAttribute("recentMessages", messagesService.findAllRecentMessages(userDTO.getId()));
        return "messages";
    }

    @GetMapping(value = "/conversation/{companionId}/{offerId}")
    public ModelAndView getConversation(@PathVariable("companionId") Long companionId,
                                        @PathVariable("offerId") Integer offerId,
                                        Model model) {

        Offer offer = offerService.findById(offerId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findUserByUserName(auth.getName());
        StringBuilder sb = new StringBuilder();
        Boolean hosted = user != null && user.getId().equals(offer.getHostId());

        if (user != null) {
            if (hosted) {
//                sb.append(offer.deleteBtn());
//                sb.append(offer.guestUI(true));
//                modelAndView.addObject("myOfferDisplay", sb.toString());
                modelAndView.setViewName("/messages");
            } else {
//                sb.append(offer.guestUI(true));
//                modelAndView.addObject("offerDisplay", sb.toString());
                addConversationToModel(companionId, model, offer);
                model.addAttribute("offer", offer);
                model.addAttribute("newMessage", new MessageDTO());
                modelAndView.setViewName("/conversation");
            }
        }

//        model.addAttribute("hosted", hosted);
//        model.addAttribute("host", "");
//        model.addAttribute("visit", "");
        return modelAndView;
    }

    @PostMapping(value = "/conversation/{companionId}/{offerId}")
    public String postMessage(@PathVariable("companionId") Long companionId,
                              @PathVariable("offerId") Integer offerId,
                              @Valid @ModelAttribute("newMessage") MessageDTO messageDTO,
                              BindingResult bindingResult,
                              Model model) {

        if (bindingResult.hasErrors()) {
            addConversationToModel(companionId, model, offerService.findById(offerId));
            return "conversation";
        }

        messageDTOCustom(messageDTO, companionId, offerId);
        return "redirect:/conversation/" + messageDTO.getReceiver().getId() + "/" + offerId;
    }

    @GetMapping(value = "/conversationHost/{companionId}/{offerId}")
    public ModelAndView getConversationHost(@PathVariable("companionId") Long companionId,
                                            @PathVariable("offerId") Integer offerId,
                                            Model model) {

        Offer offer = offerService.findById(offerId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findUserByUserName(auth.getName());

        if (user != null) {
            addConversationToModel(companionId, model, offer);
            model.addAttribute("newMessage", new MessageDTO());
            modelAndView.setViewName("/conversationHost");
        }
        return modelAndView;
    }

    @PostMapping(value = "/conversationHost/{companionId}/{offerId}")
    public String postMessageHost(@PathVariable("companionId") Long companionId,
                                  @PathVariable("offerId") Integer offerId,
                                  @Valid @ModelAttribute("newMessage") MessageDTO messageDTO,
                                  BindingResult bindingResult,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            addConversationToModel(companionId, model, offerService.findById(offerId));
            return "conversationHost";
        }

        messageDTOCustom(messageDTO, companionId, offerId);
        return "redirect:/conversationHost/" + messageDTO.getReceiver().getId() + "/" + offerId;
    }

    private void messageDTOCustom(MessageDTO messageDTO, Long companionId, Integer offerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        UserDTO userDTO = userToUserDto.convert(user);
        UserDTO companion = userService.getUserById(companionId);
        messageDTO.setSender(userDTO);
        messageDTO.setReceiver(companion);
        messageDTO.setTime(LocalDateTime.now());
        messageDTO.setOfferId(offerId);
        messagesService.postMessage(messageDTO);
    }
}
