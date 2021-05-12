package application.controller;

import application.entity.Offer;
import application.entity.OfferSearch;
import application.entity.OffersTab;
import application.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LandingController {

    @Autowired
    private OfferService offerService;

    private ModelAndView pageDisplay(Model model, String page) {
        List<Offer> offers = offerService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("offerSearch", new OfferSearch());
        model.addAttribute("offerDescriptions", offerService.findAll());

        modelAndView.addObject("offerSearch", new OfferSearch());
        ArrayList<Integer> tabsLen = new ArrayList<>();
        ArrayList<ArrayList<Offer>> lst = new ArrayList<>();
        for (int i = 0; i < offers.size() / 15; ++i) {
            ArrayList<Offer> tabList = new ArrayList<>();
            for (int j = 0; j < 15; ++j) {
                tabList.add(offers.get(j + i * 15));
            }
            lst.add(tabList);
            tabsLen.add(i);
        }

        if (offers.size() % 15 != 0) {
            ArrayList<Offer> tabList = new ArrayList<>();
            for (int j = offers.size() - 15; j < offers.size(); ++j) {
                if (j >= 0) {
                    tabList.add(offers.get(j));
                }
            }
            lst.add(tabList);
            tabsLen.add(tabsLen.size());
        }

        if (page.isBlank()) {
            model.addAttribute("offersTab", new OffersTab(lst).getList());
        } else {
            model.addAttribute("offersTab",
                    new OffersTab(lst).getList(Integer.parseInt(page)));
        }

        model.addAttribute("tabsLen", tabsLen);
        modelAndView.setViewName("landing");
        return modelAndView;
    }

    @GetMapping(value = "/")
    public ModelAndView landing(Model model) {
        return pageDisplay(model, "");
    }

    @GetMapping(value = "/page")
    public ModelAndView landing(Model model, HttpServletRequest request) {
        String page = request.getParameter("page");
        return pageDisplay(model, page);
    }

    @GetMapping(value = "/search")
    public ModelAndView registration(HttpServletRequest request, Model model) { // like common landing
        ModelAndView modelAndView = new ModelAndView();
        List<Offer> offers = new ArrayList<>();
        String noRooms = request.getParameter("noRooms");
        String roomsOne = request.getParameter("rooms1");
        String roomsTwo = request.getParameter("rooms2");
        String roomsThree = request.getParameter("rooms3");
        String roomsMore = request.getParameter("manyRooms");
        String lowerCostBound = request.getParameter("lowerCostBound");
        String higherCostBound = request.getParameter("higherCostBound");
        long lowerCost = 0L, higherCost = Long.MAX_VALUE;
        if (!lowerCostBound.isEmpty()) {
            lowerCost = Long.parseLong(lowerCostBound);
        }
        if (!higherCostBound.isEmpty()) {
            higherCost = Long.parseLong(higherCostBound);
        }

        if (noRooms != null) {
            offers.addAll(offerService.findByQuantityRoom(Integer.parseInt(noRooms)));
        }
        if (roomsOne != null) {
            offers.addAll(offerService.findByQuantityRoom(Integer.parseInt(roomsOne)));
        }
        if (roomsTwo != null) {
            offers.addAll(offerService.findByQuantityRoom(Integer.parseInt(roomsTwo)));
        }
        if (roomsThree != null) {
            offers.addAll(offerService.findByQuantityRoom(Integer.parseInt(roomsThree)));
        }
        if (roomsMore != null) {
            offers.addAll(offerService.findByQuantityRoomMoreFour());
        }

        if (!offers.isEmpty()) {
            for (Offer offer : offers) {
                if (offer.getCost() < lowerCost || offer.getCost() > higherCost) {
                    offers.remove(offer);
                }
            }
        } else {
            offers.addAll(offerService.findByCostBetween(lowerCost, higherCost));
        }

        if (noRooms == null && roomsOne == null && roomsTwo == null && roomsThree == null &&
                roomsMore == null && lowerCostBound.isEmpty() && higherCostBound.isEmpty()) {
            offers.addAll(offerService.findAll());
        }

        model.addAttribute("offerDescriptions", offers);
        modelAndView.setViewName("landing");
        return modelAndView;
    }

}