package application.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferSearch {
    private Long id;
    private String noRooms;
    private String rooms1;
    private String rooms2;
    private String rooms3;
    private String manyRooms;
    private Long lowerCostBound;
    private Long higherCostBound;
}
