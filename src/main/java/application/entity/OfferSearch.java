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
    private Boolean noRooms;
    @Column(name = "rooms1")
    private Boolean rooms1;
    @Column(name = "rooms2")
    private Boolean rooms2;
    @Column(name = "rooms3")
    private Boolean rooms3;
    @Column(name = "many_rooms")
    private Boolean manyRooms;

    @Column(name = "lower_cost_bound")
    private Long lowerCostBound;
    @Column(name = "higher_cost_bound")
    private Long higherCostBound;

//    public OfferSearch(Boolean noRooms, Boolean rooms1, Boolean rooms2, Boolean rooms3,
//                       Boolean manyRooms, Long lowerCostBound, Long higherCostBound) {
//        this.noRooms = noRooms;
//        this.rooms1 = rooms1;
//        this.rooms2 = rooms2;
//        this.rooms3 = rooms3;
//        this.manyRooms = manyRooms;
//        this.lowerCostBound = lowerCostBound;
//        this.higherCostBound = higherCostBound;
//    }
}
