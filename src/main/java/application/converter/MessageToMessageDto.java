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

    public MessageDTO convert(Message message) {
        if (message == null) return null;

        return MessageDTO.builder()
                .time(message.getTime())
                .offerId(message.getOfferId())
                .message(message.getMessage())
                .sender(userToUserDto.convert(message.getSender()))
                .roomId(message.getRoomId())
                .build();
    }
}
