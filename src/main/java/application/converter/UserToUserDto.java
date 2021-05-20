package application.converter;

import application.dto.UserDTO;
import application.entity.User;
import org.springframework.stereotype.Component;

/**
 * Класс конвертации пользователя во внутреннюю структуру
 */
@Component
public class UserToUserDto {

    /**
     * Метод конвертации
     * @param user Пользователь
     * @return Внутренняя структура
     */
    public UserDTO convert(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .lastName(user.getLastName())
                .build();
    }
}
