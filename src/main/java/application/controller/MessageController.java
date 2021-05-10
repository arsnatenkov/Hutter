package application.controller;

import application.converter.UserDtoToUser;
import application.converter.UserToUserDto;
import application.dto.MessageDTO;
import application.dto.UserDTO;
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
    private UserToUserDto userToUserDto;
    @Autowired
    private FavouriteService favouriteService;
    @Autowired
    private UserDtoToUser userDtoToUser;

    private void addConversationToModel(Long hostId, Model model, Offer offer) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        List<MessageDTO> messages = messagesService.findConversation(user.getId(), hostId, offer.getId());

        model.addAttribute("messages", messages);
        model.addAttribute("companion", userService.getUserById(hostId));
        model.addAttribute("host", offer.getHostId().equals(user.getId()));
        model.addAttribute("offer", offer);
    }

    @GetMapping(value = "/messages/{offerId}")
    public String getMessages(Model model,
                              @PathVariable("offerId") Integer offerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        UserDTO userDTO = userToUserDto.convert(user);
        Collection<MessageDTO> recentMessages;

        recentMessages = messagesService.findAllRecentMessages(userDTO.getId(), offerId);
        model.addAttribute("host", user != null
                && user.getId().equals(offerService.findById(offerId).getHostId()));
        model.addAttribute("recentMessages", recentMessages);
        model.addAttribute("emptyList", recentMessages.isEmpty());
        return "messages";
    }

    @GetMapping(value = "/conversation/{companionId}/{offerId}")
    public ModelAndView getConversation(@PathVariable("companionId") Long companionId,
                                        @PathVariable("offerId") Integer offerId,
                                        Model model) {

        Offer offer = offerService.findById(offerId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView modelAndView = new ModelAndView();
        StringBuilder sb = new StringBuilder();
        User user = userService.findUserByUserName(auth.getName());

//        if (user != null) {
//            if (user.getId().equals(offer.getHostId())) {
////                sb.append(offer.guestUI(true));
////                modelAndView.addObject("myOfferDisplay", sb.toString());
//                modelAndView.setViewName("/messages");
//            } else {
////                sb.append(offer.guestUI(true));
////                modelAndView.addObject("offerDisplay", sb.toString());
//                addConversationToModel(companionId, model, offer);
//                model.addAttribute("newMessage", new MessageDTO());
//                modelAndView.setViewName("/conversation");
//            }
//        }

        addConversationToModel(companionId, model, offer);
        model.addAttribute("newMessage", new MessageDTO());
        modelAndView.setViewName("/conversation");
        model.addAttribute("offerDisplay", offer);

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
