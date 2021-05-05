package application.dto;

import application.validation.ValidEmail;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Objects;
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotEmpty
    @ValidEmail
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO user = (UserDTO) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

