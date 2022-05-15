package application.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Objects;

/**
 * Класс-внутренняя структура для пользователя
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotEmpty
    @Email
    private String email;

    @Length(min = 5)
    private String userName;

    @NotNull
    @Length(min = 5)
    private String password;

    @NotEmpty
    private String name;

    @NotEmpty
    private String lastName;

    private Boolean active;

    private Boolean isAdmin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return id.equals(((UserDTO) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Boolean isAdmin() {
        return isAdmin;
    }
}
