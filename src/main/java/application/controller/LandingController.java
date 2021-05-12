package application.controller;

import application.entity.Offer;
import application.entity.OfferSearch;
import application.entity.OffersTab;
import application.service.OfferService;
import groovy.lang.Tuple2;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Immutable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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
    private Tuple2<List<Offer>, List<Integer>> preprocess(List<Offer> offers, String page){

        List<Integer> tabsLen = new ArrayList<>();
        List<List<Offer>> lst = new ArrayList<>();
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
        if(offers.isEmpty()){
            return new Tuple2<>(new ArrayList<>(), tabsLen);
        }
        return new Tuple2<>(lst.get(Integer.parseInt(page)), tabsLen);
    }
    @GetMapping(value = "/")
    public ModelAndView landing(Model model, HttpServletRequest request) {

        String page = request.getParameter("page");
        if(page == null){
            page = "0";
        }
        List<Offer> offers = offerService.findAll();
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("offerSearch", new OfferSearch());
        Tuple2<List<Offer>, List<Integer>> tuple = preprocess(offers, page);
        model.addAttribute("offersTab", tuple.getFirst());
        model.addAttribute("tabsLen", tuple.getSecond());
        modelAndView.setViewName("landing");
        return modelAndView;
    }

    @GetMapping(value = "/search")
    public ModelAndView search(HttpServletRequest request, Model model) { // like common landing
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
        Tuple2<List<Offer>, List<Integer>> tuple = preprocess(offers, "0");
        model.addAttribute("offersTab", tuple.getFirst());
        model.addAttribute("tabsLen", tuple.getSecond());
        modelAndView.addObject("offerSearch", new OfferSearch());
        modelAndView.setViewName("landing");
        return modelAndView;
    }

}