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
@Table(name = "room_chat")
public class RoomChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "offer_id")
    @NotNull
    private Long offerId;

    @Column(name = "user_id")
    @NotNull
    private Long userId;

    @Column(name = "room_id")
    @NotNull
    private Long roomId;
}
