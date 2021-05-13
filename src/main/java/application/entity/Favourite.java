package application.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "favourite")
public class Favourite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "offer_id")
    @NotNull
    private Long offerId;

    @Column(name = "user_id")
    @NotNull
    private Long userId;

    @Column(name = "address")
    @NotNull
    public String address;

    public Favourite(Long userId, Long offerId, String address){
        this.offerId = offerId;
        this.userId = userId;
        this.address = address;
    }
}
