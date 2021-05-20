package application.controller;

import application.converter.UserToUserDto;
import application.dto.AddDTO;
import application.dto.MessageDTO;
import application.dto.UserDTO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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

    //    private void addConversationToModel(Long hostId, Model model, Offer offer) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findUserByUserName(auth.getName());
//        List<MessageDTO> messages =
//                messagesService.findConversation(user.getId(), hostId, offer.getId());
//
//        model.addAttribute("messages", messages);
//        model.addAttribute("companion", userService.getUserById(hostId));
//        model.addAttribute("host", offer.getHostId().equals(user.getId()));
//        model.addAttribute("offer", offer);
//    }
    private void addConversationToModel(Long roomId, Model model, Offer offer) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        List<MessageDTO> messages = messagesService.findMessageByRoomId(roomId);
        model.addAttribute("messages", messages);
        model.addAttribute("currentUser", user);
        model.addAttribute("room", roomId);
        model.addAttribute("companion", offer.getHostId());
        model.addAttribute("host", offer.getHostId().equals(user.getId()));
        model.addAttribute("offer", offer);
    }

    @GetMapping(value = "/messages/{offerId}")
    public String getMessages(Model model,
                              @PathVariable("offerId") Long offerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        UserDTO userDTO = userToUserDto.convert(user);
        Collection<MessageDTO> recentMessages;
        String address = offerService.findById(offerId).get().getAddress();
        recentMessages = messagesService.findAllRecentMessages(offerId);
        model.addAttribute("host", user != null
                && user.getId().equals(offerService.findById(offerId).get().getHostId()));
        model.addAttribute("recentMessages", recentMessages);
        model.addAttribute("emptyList", recentMessages.isEmpty());
        model.addAttribute("address", address);
        return "messages";
    }

    @GetMapping(value = "/conversation/{roomId}/{offerId}")
    public ModelAndView getConversation(@PathVariable("roomId") Long roomId,
                                        @PathVariable("offerId") Long offerId,
                                        Model model) {
        Optional<Offer> offers = offerService.findById(offerId);
        Offer offer = offers.get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findUserByUserName(auth.getName());
        if (user != null) {
            addConversationToModel(roomId, model, offer);
            model.addAttribute("newMessage", new MessageDTO());
            modelAndView.addObject("userAdd", new AddDTO());
            modelAndView.setViewName("conversation");
        }

        return modelAndView;

    }

    @PostMapping(value = "/conversation/{roomId}/{offerId}")
    public String postMessage(@PathVariable("offerId") Long offerId,
                              @PathVariable("roomId") Long roomId,
                              @Valid @ModelAttribute("newMessage") MessageDTO messageDTO,
                              BindingResult bindingResult,
                              Model model) {

        if (bindingResult.hasErrors()) {
            Offer offer = offerService.findById(offerId).get();
            addConversationToModel(roomId, model, offer);
            return "conversation";
        }
        messageDTOCustom(messageDTO, roomId, offerId);
        return "redirect:/conversation/" + roomId + "/" + offerId;
    }

//    @GetMapping(value = "/conversation/{companionId}/{offerId}")
//    public ModelAndView getConversation(@PathVariable("companionId") Long companionId,
//                                            @PathVariable("offerId") Long offerId,
//                                            Model model) {
//
//        Optional<Offer> offers = offerService.findById(offerId);
//        Offer offer = offers.get();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        ModelAndView modelAndView = new ModelAndView();
//        User user = userService.findUserByUserName(auth.getName());
//
//        if (user != null) {
//            addConversationToModel(companionId, model, offer);
//            model.addAttribute("newMessage", new MessageDTO());
//            modelAndView.addObject("userAdd", new AddDTO());
//            modelAndView.setViewName("conversation");
//        }
//        return modelAndView;
//    }
//
//    @PostMapping(value = "/conversation/{companionId}/{offerId}")
//    public String postMessage(@PathVariable("companionId") Long companionId,
//                                  @PathVariable("offerId") Long offerId,
//                                  @Valid @ModelAttribute("newMessage") MessageDTO messageDTO,
//                                  BindingResult bindingResult,
//                                  Model model) {
//
//        if (bindingResult.hasErrors()) {
//            Offer offer = offerService.findById(offerId).get();
//            addConversationToModel(companionId, model, offer);
//            return "conversation";
//        }
//        messageDTOCustom(messageDTO, companionId, offerId);
//        return "redirect:/conversation/" + messageDTO.getReceiver().getId() + "/" + offerId;
//    }

    private void messageDTOCustom(MessageDTO messageDTO, Long roomId, Long offerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        UserDTO userDTO = userToUserDto.convert(user);
        messageDTO.setSender(userDTO);
        messageDTO.setTime(LocalDateTime.now());
        messageDTO.setOfferId(offerId);
        messageDTO.setRoomId(roomId);
        messagesService.postMessage(messageDTO);
    }

    @GetMapping(value = "/add/{roomId}")
    public ModelAndView addUserInConversation(@PathVariable("roomId") Long roomId, HttpServletRequest request, Model model) {
        ModelAndView modelAndView = new ModelAndView();
        String userName = request.getParameter("userName");
        Long offerId = Long.parseLong(request.getParameter("offerId"));
        Offer offer = offerService.findById(offerId).get();
        User user = userService.findUserByUserName(userName);
        messagesService.findMessageByUserIdAndOfferId(user.getId(), offerId, roomId);
        addConversationToModel(roomId, model, offer);
        modelAndView.addObject("userAdd", new AddDTO());
        model.addAttribute("newMessage", new MessageDTO());
        modelAndView.setViewName("conversation");
        return modelAndView;
    }
}
