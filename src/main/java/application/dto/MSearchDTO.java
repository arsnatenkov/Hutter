package application.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Класс-внутренняя структура для поиска объявлений
 */
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MSearchDTO {
    private Long id;
    private String sender;
    private Long offerId;
}
