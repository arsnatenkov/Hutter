package application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "offers")
public class Offer {

    //TODO: указать этаж!!!

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "offer_id")
    private Integer id;
    @Column(name = "public_id")
    private Integer publicId;
    @Column(name = "total_area")
    private Long totalArea;
    @Column(name = "living_space")
    private Long living;
    @Column(name = "room_area")
    private String roomArea;
    @Column(name = "quantity_of_room") //TODO: roomsNumber
    private Integer quantityRoom;
    @Column(name = "cost")
    private Long cost;
    @Column(name = "quantity_of_toilet")
    private Integer quantityToilet;
    @Column(name = "type_of_toilet")
    private String type;
    @Column(name = "material")
    private String material;
    @Column(name = "address")
    private String address;
    @Column(name = "coordinate_X")
    private String coordinateX;
    @Column(name = "coordinate_Y")
    private String coordinateY;
    @Column(name = "description")
    private String description;
    @Column(name = "host_id")
    private Integer hostId;
    @Column(name = "rooms_spaces")
    private String roomsSpaces;
    @Column(name = "has_parking")
    private Boolean hasParking;
    @Column(name = "parking_type")
    private String parkingType;
    @Column(name = "floor")
    private Integer floor;
    @Column(name = "floor_max")
    private Integer floorMax;


    @Override
    public String toString() {
        return getPublicId() + ":"
                + getTotalArea() + ":"
                + getLiving() + ":"
                + getRoomArea() + ":"
                + getQuantityRoom() + ":"
                + getCost() + ":"
                + getQuantityToilet() + ":"
                + getType() + ":"
                + getMaterial() + ":"
                + getAddress() + ":"
                + getCoordinateX() + ":"
                + getCoordinateY() + ":"
                + getHostId() + ":"
                + getDescription() + ";";
    }

    public String linkTitle() {
        return makeLink(getPublicId(), "offer", getAddress()) + "<br/>";
    }

    public String longDescription() {
        String res = icon("ruble") + "цена: " + getCost() + " ₽<br/><br/>";

        res += icon("plans") + "общая площадь: <span class=\"space\">" + getTotalArea() + " м²</span><br/>";
        res += "жилая площадь: " + getLiving() + " м²<br/><br/>";

        res += icon("living-room") + "кол-во комнат: " + getQuantityRoom() + "<br/>";
        res += "площади комнат: " + getRoomArea() + " м²<br/><br/>";

        res += icon("bathroom") + "кол-во санузлов: " + getQuantityToilet() + "<br/>";
        res += "типы санузлов: " + getType() + "<br/><br/>";

        res += icon("brick") + "строительные материалы: " + getMaterial() + "<br/><br/>";

        String description = getDescription();
//        String description = getDescription().length() < 50 ? getDescription() :
//                getDescription().substring(0, 47) + "...";

        res += icon("document") + "описание: " + description + "<br/><br/>";

        res += "<a href=\"chat" + getHostId() + "\" class=\"login-form-btn chat\">" +
                "Начать чат с владельцем</a><br />";
        return res;
    }

    public String shortDescription() {
        String res = icon("ruble") + "цена: " + getCost() + " ₽<br/><br/>";

        res += icon("plans") + "общая площадь: <span class=\"space\">" + getTotalArea() + " м²</span><br/>";

        res += icon("living-room") + "кол-во комнат: " + getQuantityRoom() + "<br/>";

        res += icon("bathroom") + "кол-во санузлов: " + getQuantityToilet() + "<br/>";

        res += "<a href=\"chat" + getHostId() + "\" class=\"login-form-btn chat\">" +
                "Начать чат с владельцем</a><br />";
        return res;
    }

    static public String icon(String name) {
        return "<img class=\"offer-icons\" src=\"images/" + name +
                ".svg\" alt=\"" + name + "\">&nbsp;";
    }

    static public String makeLink(int id, String type, String text) {
        return "<a class=\"" + type + "\" id=\"" + id + "\" href=\"/" +
                type + "?id=" + id + "\">" + text + "</a>";
    }

    // общая площадь, жилая площадь, площадь кухни, площади комнат,
    // кол-во комнат, стоимость, сан. узлы, типы сан. узлов, материал здания,
    // адрес, координаты, описание, айди оффера публичный, айди оффера закрытый,
    // айди продавца публичный
    // * метро
}
