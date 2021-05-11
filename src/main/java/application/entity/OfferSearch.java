package application.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "offerSearches")
public class OfferSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "no_rooms")
    private String noRooms;
    @Column(name = "rooms1")
    private String rooms1;
    @Column(name = "rooms2")
    private String rooms2;
    @Column(name = "rooms3")
    private String rooms3;
    @Column(name = "many_rooms")
    private String manyRooms;
    @Column(name = "lower_cost_bound")
    private Long lowerCostBound;
    @Column(name = "higher_cost_bound")
    private Long higherCostBound;
}
