package application.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Класс-внутренняя структура для сообщений
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {

    private LocalDateTime time;
    private Long offerId;
    @NotNull
    @Size(min = 2, max = 3000)
    private String message;
    private UserDTO sender;
    private Long roomId;
}
