package application.controller;

import application.entity.Offer;
import application.entity.User;
import application.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.awt.*;
import java.util.List;

@Controller
public class LandingController {

    @Autowired
    private OfferService offerService;

    @GetMapping("/")
    public ModelAndView landing() {
        ModelAndView modelAndView = new ModelAndView();
        List<Offer> offers = offerService.findAll();
        StringBuilder sb = new StringBuilder();

        for (Offer offer : offers)
            sb.append(wrap(offer));

        modelAndView.addObject("offerDescriptions", sb.toString());
        modelAndView.setViewName("landing");
        return modelAndView;
    }

    private String wrap(Offer offer) {
        String res = "<li><table><tr>" +
                "<td class=\"offer-pic\">" +
                "<img src=\"images/" + offer.getPublicId() + "\" alt=\"Фото объявления\"/>" +
                "<form th:action=\"@{/}\" method=\"GET\" class=\"little-form\">" +
                "<button class=\"login-form-btn\">Войти</button></form></td>";

        res += "<td>" + makeLink(offer.getPublicId(), "offer", offer.getAddress()) + "<br />";
        res += "цена: " + offer.getCost() + " ₽<br />";
        res += "общая площадь: <span class=\"space\">" + offer.getTotalArea() + " м²</span><br />";
        res += "жилая площадь: " + offer.getLiving() + " м²<br />";
        res += "кол-во комнат: " + offer.getQuantityRoom() + "<br />";
        res += "площади комнат: " + offer.getRoomArea() + " м²<br />";
        res += "кол-во санузлов: " + offer.getQuantityToilet() + "<br />";
        res += "типы санузлов: " + offer.getType() + "<br />";
        res += "строительные материалы: " + offer.getMaterial() + "<br />";
        res += "описание: " + offer.getDescription() + "<br />";
        res += makeLink(offer.getHostId(), "user", "Владелец") + "<br />";

        return res + "</td></tr></table></li>";
    }

    private String makeLink(int id, String type, String text) {
        return "<a class=\"" + type + "\" id=\"" + id + "\" href=\"/" + type + "?id=" + id + "\">" + text + "</a>";
    }
}