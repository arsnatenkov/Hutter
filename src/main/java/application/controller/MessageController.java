package application.controller;

import application.converter.UserDtoToUser;
import application.converter.UserToUserDto;
import application.dto.MessageDTO;
import application.dto.UserDTO;
import application.entity.Message;
import application.entity.User;
import application.service.MessageService;
import application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static application.utils.ServerUtils.getUserFromSession;

@Controller
@RequiredArgsConstructor
public class MessageController {
    @Autowired
    private MessageService messagesService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDtoToUser userDtoToUser;
    @Autowired
    private UserToUserDto userToUserDto;

    private void addConversationToModel(Long hostId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        UserDTO userDTO = userToUserDto.convert(user);
        UserDTO companion = userService.getUserById(hostId);
        List<MessageDTO> messages;
        if(messagesService.findConversation(userDTO.getId(), hostId) != null){
            messages = messagesService.findConversation(userDTO.getId(), hostId);
        }else{
            messagesService.saveMessage(new Message(user, userDtoToUser.convert(userService.getUserById(hostId))));
            messages = messagesService.findConversation(user.getId(), hostId);
        }

        model.addAttribute("messages", messages);
        model.addAttribute("companion", companion);
    }

    @GetMapping("/messages")
    public String getMessages(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        UserDTO userDTO = userToUserDto.convert(user);
        Collection<MessageDTO> recentMessages;
        if(messagesService.findAllRecentMessages(userDTO.getId()) != null){
            recentMessages = messagesService.findAllRecentMessages(userDTO.getId());
        }else{
            messagesService.saveMessage(new Message());
            recentMessages = messagesService.findAllRecentMessages(userDTO.getId());
        }
        model.addAttribute("recentMessages", recentMessages);
        return "messages";
    }

    @GetMapping("/conversation/{companionId}")
    public String getConversation(@PathVariable Long companionId, HttpServletRequest request, Model model) {
        addConversationToModel(companionId, model);
        model.addAttribute("newMessage", new MessageDTO());
        return "conversation";
    }

    @PostMapping("/conversation/{companionId}")
    public String postMessage(@PathVariable Long companionId,
                              @Valid @ModelAttribute("newMessage") MessageDTO messageDTO, BindingResult bindingResult,
                              Model model) {
        if(bindingResult.hasErrors()) {
            addConversationToModel(companionId, model);
            return "conversation";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        UserDTO userDTO = userToUserDto.convert(user);
        UserDTO companion = userService.getUserById(companionId);
        messageDTO.setSender(userDTO);
        messageDTO.setReceiver(companion);
        messageDTO.setTime(LocalDateTime.now());
        messagesService.postMessage(messageDTO);
        return "redirect:/conversation/" + messageDTO.getReceiver().getId();
    }
}
