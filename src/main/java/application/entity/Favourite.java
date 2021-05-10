package application.entity;

import lombok.*;

import javax.persistence.*;

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
    private Integer offerId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "address")
    public String address;

    public Favourite(Long userId, Integer offerId, String address){
        this.offerId = offerId;
        this.userId = userId;
        this.address = address;
    }


}
