package application.converter;

import application.dto.UserDTO;
import application.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDto {

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
