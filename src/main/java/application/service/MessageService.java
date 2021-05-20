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
    public Collection<MessageDTO> findAllRecentMessages(Long offerId) {
        Iterable<Message> all = messageRepository.findByOfferId(offerId);

        Map<Long, MessageDTO> map = new HashMap<>();

        all.forEach(m -> {
            MessageDTO messageDTO = messageToMessageDto.convert(m);
            map.put(m.getRoomId(), messageDTO);
        });

        return map.values();
    }

    @Transactional
    public List<MessageDTO> findMessageByRoomId(Long roomId){
        List<Message> all = messageRepository.findMessageByRoomId(roomId);
        List<MessageDTO> messages = new LinkedList<>();
        if (all != null) {
            all.forEach(m -> messages.add(messageToMessageDto.convert(m)));
        }

        return messages;
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

    public Long findRoom(Long offerId, Long userId){
        List<Message> messages = messageRepository.findRoom(offerId, userId);
        List<Message> room = messageRepository.findAll();
        if(messages.isEmpty()){
            if(room.isEmpty()){
                return 0L;
            }
            return room.get(room.size() - 1).getRoomId() + 1;
        }
        return messages.get(0).getRoomId();
    }

    public void findMessageByUserIdAndOfferId(Long userId, Long offerId, Long roomId){
        List<Message> messages = messageRepository.findMessageByUserIdAndOfferId(userId, offerId);
        messages.forEach(m -> new Message(m.getId(), m.getOfferId(), m.getTime(), m.getMessage(), m.getSender(), m.getRoomId()));
        messages.forEach(m -> m.setRoomId(roomId));
    }

    public void deleteMessageByUserIdAndOfferId(Long userId, Long offerId, Long roomId){
        List<Message> messages = messageRepository.findMessageByUserIdAndOfferId(userId, offerId);
        messages.forEach(m -> messageRepository.delete(m));
    }
}
