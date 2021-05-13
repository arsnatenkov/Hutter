package application.converter;

import application.dto.MessageDTO;
import application.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageDtoToMessage {
    @Autowired
    private UserDtoToUser userDtoToUser;

    public Message convert(MessageDTO messageDTO) {
        if (messageDTO == null) return null;

        return Message.builder()
                .offerId(messageDTO.getOfferId())
                .time(messageDTO.getTime())
                .message(messageDTO.getMessage())
                .sender(userDtoToUser.convert(messageDTO.getSender()))
                .receiver(userDtoToUser.convert(messageDTO.getReceiver()))
                .build();
    }
}
