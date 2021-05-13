package application.service;

import application.converter.MessageDtoToMessage;
import application.converter.MessageToMessageDto;
import application.dto.MessageDTO;
import application.entity.Message;
import application.entity.User;
import application.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private final MessageToMessageDto messageToMessageDto;
    @Autowired
    private final MessageDtoToMessage messageDtoToMessage;

    @Transactional(readOnly = true)
    public Collection<MessageDTO> findAllRecentMessages(Long id, Long offerId) {
        Iterable<Message> all = messageRepository.findAllRecentMessages(id, offerId);
        Map<User, MessageDTO> map = new HashMap<>();

        all.forEach(m -> {
            MessageDTO messageDTO = messageToMessageDto.convert(m, id);
            User user = m.getSender().getId().equals(id) ? m.getReceiver() : m.getSender();
            map.put(user, messageDTO);
        });

        return map.values();
    }

    @Transactional(readOnly = true)
    public List<MessageDTO> findConversation(Long userId, Long companionId, Long offerId) {
        List<Message> all = messageRepository.findConversation(userId, companionId, offerId);
        List<MessageDTO> messages = new LinkedList<>();

        if (all != null) {
            all.forEach(m -> messages.add(messageToMessageDto.convert(m, userId)));
        }

        return messages;
    }

    @Transactional(readOnly = true)
    public MessageDTO getRecentMessage(Long id) {
        Message message = messageRepository.findFirstBySenderIdOrReceiverIdOrderByIdDesc(id, id);

        return messageToMessageDto.convert(message, id);
    }

    @Transactional
    public void postMessage(MessageDTO messageDTO) {
        Message message = messageDtoToMessage.convert(messageDTO);
        messageRepository.save(message);
    }

    public List<Message> findByOfferId(Long offerId) {
        return messageRepository.findByOfferId(offerId);
    }

    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }
}
