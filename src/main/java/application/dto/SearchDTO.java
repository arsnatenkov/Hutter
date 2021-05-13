package application.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {
    private Long id;
    private String noRooms;
    private String rooms1;
    private String rooms2;
    private String rooms3;
    private String manyRooms;
    private Long lowerCostBound;
    private Long higherCostBound;
}
