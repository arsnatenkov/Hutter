package application.converter;

import application.dto.MessageDTO;
import application.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageToMessageDto {
    @Autowired
    private UserToUserDto userToUserDto;

    public MessageDTO convert(Message message, Long id) {
        if (message == null) return null;

        return MessageDTO.builder()
                .time(message.getTime())
                .offerId(message.getOfferId())
                .message(message.getMessage())
                .receiver(userToUserDto.convert(message.getReceiver()))
                .sender(userToUserDto.convert(message.getSender()))
                .companionId(id.equals(message.getSender().getId()) ?
                        message.getReceiver().getId() : message.getSender().getId())
                .build();
    }
}
