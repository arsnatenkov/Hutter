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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "offer_id")
    private Integer id;
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
    private Long hostId;
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
        return getId() + ":"
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

    public String linkTitle(String aClass, String map) {
        if (map.equals("conversation")) {
            return "<a class=\"offer " + aClass + "\" id=\"" + getId() + "\" href=\"/" + map +
                    "/" + getHostId() + "/" + getId() + "\">" + getAddress() + "</a>";
        }
        if (map.equals("messages")) {
            return "<a class=\"offer " + aClass + "\" id=\"" + getId() + "\" href=\"/" + map +
                    "\">" + getAddress() + "</a>";
        }
        if (map.equals("offer")) {
            return "<a class=\"offer " + aClass + "\" id=\"" + getId() + "\" href=\"/" + map +
                    "?id=" + getId() + "\">" + getAddress() + "</a>";
        }
        return "<a class=\"offer " + aClass + "\" id=\"" + getId() + "\" href=\"/error" + "</a>";
    }

    public String deleteBtn() {
        return "<div class=\"btn f-group-btn offer-delete\">" +
                "<img class=\"invert\" alt=\"delete\" src=\"/images/delete.svg\">" +
                "</div>";
//        return "<div class=\"hostUI\"><a href=/edit?id=" + getId() + ">Изменить</a></div>";
    }

    public String saveBtn() {
//        btn f-group-btn
        return "<div id=\"save\" class=\"btn f-group-btn\">" +
                "<a href=\"/save\">" +
                "<img class=\"invert\" alt=\"save\" src=\"/images/save.svg\">" +
                "</a>" +
                "</div>";
//        return "<div id=\"save\" class=\"f-group-btn-wrap\">" +
//                "<a href=\"/save\" class=\"btn f-group-btn\">" +
//                "<img class=\"invert\" alt=\"save\" src=\"/images/save.svg\">" +
//                "</a>" +
//                "</div>";
    }

    public String longDescription() {
        String res = "";
        res += "<div class=\"center-div scroll\">";
        res += "<div class=\"inner-div\"><div class=\"text-container\">";
        res += icon("ruble") + "цена: " + (getCost() == null ?
                "не указана" : (getCost() + " ₽")) + "<br/><br/>";

        res += icon("plans") + "общая площадь: <span class=\"space\">" + (getTotalArea() == null ? "не указана" : (getTotalArea() + " м²")) + "</span><br/>";
        res += "жилая площадь: " + (getLiving() == null ? "не указана" : (getLiving() + " м²")) + "<br/><br/>";

        res += icon("floor") + "этаж: " + (getFloor()==null?"не указан":getFloor()) + "<br/>";
        res += "высота здания: " + (getFloorMax() == null ? "не указана" : getFloorMax()) + "<br/><br/>";

        res += icon("living-room") + "кол-во комнат: " + (getQuantityRoom() == null ? "не указано" : getQuantityRoom()) + "<br/>";
        res += "площади комнат: " + (getRoomArea() == null ? "не указана" : (getRoomArea() + " м²")) + "<br/><br/>";

        res += icon("bathroom") + "кол-во санузлов: " + (getQuantityToilet() == null ? "не указано" : getQuantityToilet()) + "<br/>";
        res += "типы санузлов: " + (getType() == null ? "не указана" : getType()) + "<br/><br/>";

        res += icon("brick") + "строительные материалы: " + (getMaterial() == null ? "не указаны" : getMaterial()) + "<br/><br/>";

        if (getHasParking() != null && getHasParking()) {
            res += icon("parking") + "парковка: есть<br/>";
            res += "тип парковки: " + getParkingType() + "<br/><br/>";
        } else {
            res += icon("parking") + "парковка: отсутствует<br/><br/>";
        }

        res += icon("document") + "описание: " + (getDescription() == null ? "не указано" : getDescription()) + "<br/><br/>";

        res += "</div>";
        res += "</div>";
        res += "</div>";
        return res;
    }

    public String shortDescription(String map) {
        String res = icon("ruble") + "цена: " + (getCost() == null ?
                "не указана" : (getCost() + " ₽")) + "<br/><br/>";
        res += icon("plans") + "общая площадь: <span class=\"space\">" +
                (getTotalArea() == null ? "не указана" : (getTotalArea() + " м²")) + "</span><br/><br/>";

        res += icon("floor") + "этаж: " + (getFloor()==null?"не указан":getFloor()) + "<br/><br/>";

        res += icon("living-room") + "кол-во комнат: " + (getQuantityRoom() == null ? "не указано" : getQuantityRoom()) + "<br/><br/>";

        res += icon("bathroom") + "кол-во санузлов: " + (getQuantityToilet() == null ? "не указано" : getQuantityToilet()) + "<br/><br/>";
        if (map == "conversation") {
            res += "<a href=\"/" + map + "/" + getHostId() + "/" + getId() + "\" class=\"login-form-btn chat\">" +
                    "Подробнее</a>";
        }
        if (map == "messages") {
            res += "<a href=\"/" + map + "\" class=\"login-form-btn chat\">" +
                    "Подробнее</a>";
        }
        if (map == "offer") {
            res += "<a href=\"/" + map + "?id=" + getId() + "\" class=\"login-form-btn chat\">" +
                    "Подробнее</a>";
        }
        return res;
    }

    static public String icon(String name) {
//        return "";
        return "<img class=\"offer-icons\" src=\"/images/" + name +
                ".svg\" alt=\"" + name + "\">&nbsp;";
    }

    public String guestUI(boolean auth) {
        String title = getAddress() + ", " + getTotalArea() + "м²";
        if (auth) {
            title += saveBtn();
        }
        String body = longDescription() + "<br/>";
        return "<h2>" + title + "</h2><hr>" + body + "<br/>";
    }

    // общая площадь, жилая площадь, площадь кухни, площади комнат,
    // кол-во комнат, стоимость, сан. узлы, типы сан. узлов, материал здания,
    // адрес, координаты, описание, айди оффера публичный, айди оффера закрытый,
    // айди продавца публичный
    // * метро
}
