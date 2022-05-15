package application.dto;

import lombok.*;

/**
 * Класс-внутренняя структура для поиска объявлений
 */
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class USearchDTO {
    private Long id;
    private String email;
    private String userName;
    private String name;
    private String lastName;
    private Boolean active;
}
