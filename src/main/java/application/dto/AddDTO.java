package application.dto;

import lombok.*;

/**
 * Класс-внутренняя структура для добавления пользователя в переписку
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddDTO {
    private String userName;
}
