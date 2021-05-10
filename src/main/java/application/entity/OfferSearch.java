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

//    @Column(name = "no_rooms")
//    private String noRooms;
//    @Column(name = "rooms1")
//    private String rooms1;
//    @Column(name = "rooms2")
//    private String rooms2;
//    @Column(name = "rooms3")
//    private String rooms3;
//    @Column(name = "many_rooms")
//    private String manyRooms;

    @Column(name = "rooms")
    private String rooms = "";

    @Column(name = "lower_cost_bound")
    private Long lowerCostBound = Long.parseLong("0");
    @Column(name = "higher_cost_bound")
    private Long higherCostBound = Long.parseLong("1");

    public OfferSearch(String rooms, Long lowerCostBound, Long higherCostBound) {
        this.rooms = rooms;
        this.lowerCostBound = lowerCostBound;
        this.higherCostBound = higherCostBound;
    }

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
