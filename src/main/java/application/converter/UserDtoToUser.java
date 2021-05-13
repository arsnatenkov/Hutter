package application.converter;

import application.dto.UserDTO;
import application.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDtoToUser {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User convert(UserDTO userDTO) {
        User.UserBuilder builder = User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .name(userDTO.getName())
                .lastName(userDTO.getLastName());

        String password = userDTO.getPassword();

        if (password != null) builder.password(passwordEncoder.encode(password));

        return builder.build();
    }
}
