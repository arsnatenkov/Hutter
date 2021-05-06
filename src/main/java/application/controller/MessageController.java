package application.controller;

import application.converter.UserDtoToUser;
import application.converter.UserToUserDto;
import application.dto.MessageDTO;
import application.dto.UserDTO;
import application.entity.Message;
import application.entity.Offer;
import application.entity.User;
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

import javax.servlet.http.HttpServletRequest;
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

    private void addConversationToModel(Long hostId, Model model, Offer offer) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        UserDTO userDTO = userToUserDto.convert(user);
        UserDTO companion = userService.getUserById(hostId);
        List<MessageDTO> messages;
        if (messagesService.findConversation(userDTO.getId(), hostId, offer.getId()) != null) {
            messages = messagesService.findConversation(userDTO.getId(), hostId, offer.getId());
        } else {
            messagesService.saveMessage(new Message(user, userDtoToUser.convert(userService.getUserById(hostId))));
            messages = messagesService.findConversation(user.getId(), hostId, offer.getId());
        }

        model.addAttribute("messages", messages);
        model.addAttribute("companion", companion);
        model.addAttribute("offer", offer);
    }

    @GetMapping(value = "/messages")
    public String getMessages(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        UserDTO userDTO = userToUserDto.convert(user);
        Collection<MessageDTO> recentMessages;
        if (messagesService.findAllRecentMessages(userDTO.getId()) != null) {
            recentMessages = messagesService.findAllRecentMessages(userDTO.getId());
        } else {
            messagesService.saveMessage(new Message());
            recentMessages = messagesService.findAllRecentMessages(userDTO.getId());
        }
        model.addAttribute("recentMessages", recentMessages);
        return "messages";
    }

    @GetMapping(value = "/conversation/{companionId}/{offerId}")
    public ModelAndView getConversation(@PathVariable("companionId") Long companionId,
                                        @PathVariable("offerId") Integer offerId,
                                        HttpServletRequest request, Model model) {
        Offer offer = offerService.findById(offerId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView modelAndView = new ModelAndView();
        StringBuilder sb = new StringBuilder();
        User user = userService.findUserByUserName(auth.getName());
        if (user != null) {
            if (user.getId().equals(offer.getHostId())) {
                sb.append(offer.editBtn());
                sb.append(offer.guestUI(true));
                modelAndView.addObject("myOfferDisplay", sb.toString());

                modelAndView.setViewName("/messages");
            } else {
                sb.append(offer.guestUI(true));
                modelAndView.addObject("offerDisplay", sb.toString());
                addConversationToModel(companionId, model, offer);
                model.addAttribute("newMessage", new MessageDTO());

                modelAndView.setViewName("/conversation");
            }
        }

        return modelAndView;
    }

    @PostMapping(value = "/conversation/{companionId}/{offerId}")
    public String postMessage(@PathVariable("companionId") Long companionId, @PathVariable("offerId") Integer offerId,
                              @Valid @ModelAttribute("newMessage") MessageDTO messageDTO, BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            addConversationToModel(companionId, model, offerService.findById(offerId));
            return "conversation";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        UserDTO userDTO = userToUserDto.convert(user);
        UserDTO companion = userService.getUserById(companionId);
        messageDTO.setSender(userDTO);
        messageDTO.setReceiver(companion);
        messageDTO.setTime(LocalDateTime.now());
        messageDTO.setOfferId(offerId);
        messagesService.postMessage(messageDTO);
        return "redirect:/conversation/" + messageDTO.getReceiver().getId() + "/" + offerId;
    }
}
