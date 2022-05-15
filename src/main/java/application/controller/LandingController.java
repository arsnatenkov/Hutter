package application.controller;

import application.dto.SearchDTO;
import application.dto.UserDTO;
import application.entity.Offer;
import application.service.OfferService;
import application.service.UserService;
import groovy.lang.Tuple2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.min;

@Controller
@RequiredArgsConstructor
public class LandingController {

    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    /**
     * Метод для возврата 15 объявлений размещенных на текущей странице
     * @param offers Список объявлений
     * @param page Текущая страница
     * @param userDTO Текущая страница
     * @return Спосиок объявлений
     */
    private List<Offer> getTab(List<Offer> offers, int page, UserDTO userDTO) {
        List<Offer> tab = new ArrayList<>();

        for (int i = page * 15; i < min(page * 15 + 15, offers.size()); ++i)
            tab.add(offers.get(i));

        return tab;
    }

    /**
     * Метод для разбиения списка объявлений на странице
     * @param offers Список объявлений
     * @param page Номер страницы
     * @return Список объявлений, список страниц
     */
    private Tuple2<List<Offer>, List<Integer>> preprocess(List<Offer> offers, int page, UserDTO userDTO) {
        List<Integer> tabsLen = new ArrayList<>();
        int maxLen = offers.size() / 15 + (offers.size() % 15 == 0 ? 0 : 1);

        for (int i = 0; i < maxLen; ++i)
            tabsLen.add(i);

        if (page >= maxLen)
            return new Tuple2<>(getTab(offers, 0, userDTO), tabsLen);

        return new Tuple2<>(getTab(offers, page, userDTO), tabsLen);
    }

    /**
     * Метод перехода на главную страницу
     * @param model Модель страницы
     * @param request Параметры запроса
     * @return Модель страницы
     */
    @GetMapping(value = "/")
    public ModelAndView landing(Model model, HttpServletRequest request, UserDTO userDTO) {
        String param = request.getParameter("page");
        int page = param == null ? 0 : Integer.parseInt(param);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("offerSearch", new SearchDTO());

        List<Offer> offers = offerService.findAll();
        Tuple2<List<Offer>, List<Integer>> tuple = preprocess(offers, page, userDTO);

        model.addAttribute("offersTab", tuple.getFirst());
        model.addAttribute("tabsLen", tuple.getSecond());

        modelAndView.setViewName("landing");
        return modelAndView;
    }

    /**
     * Метод поиска объявлений на главной странице
     * @param request Параметры запроса
     * @param model Модель страницы
     * @return Модель страницы
     */
    @GetMapping(value = "/search")
    public ModelAndView search(HttpServletRequest request, Model model, UserDTO userDTO) {
        ModelAndView modelAndView = new ModelAndView();
        List<Offer> offers = new ArrayList<>();
        List<String> rooms = new ArrayList<>();
        rooms.add(request.getParameter("noRooms"));
        rooms.add(request.getParameter("rooms1"));
        rooms.add(request.getParameter("rooms2"));
        rooms.add(request.getParameter("rooms3"));
        rooms.add(request.getParameter("manyRooms"));

        String lowerBound = request.getParameter("lowerCostBound");
        String higherBound = request.getParameter("higherCostBound");
        final long lowerCost = lowerBound.isEmpty() ? 0L : Long.parseLong(lowerBound);
        final long higherCost = higherBound.isEmpty() ? Long.MAX_VALUE : Long.parseLong(higherBound);

        for (int i = 0; i < 4; ++i) {
            if (rooms.get(i) != null) {
                offers.addAll(offerService.findByQuantityRoom(Integer.parseInt(rooms.get(i))));
            }
        }
        if(rooms.get(4) != null){
            offers.addAll(offerService.findByQuantityRoomMoreFour());
        }



        if (offers.isEmpty() && lowerCost != 0L && higherCost != Long.MAX_VALUE) {
            offers.addAll(offerService.findByCostBetween(lowerCost, higherCost));
        } else {
            offers.removeIf(o -> o.getCost() < lowerCost || o.getCost() > higherCost);
        }

        if (rooms.stream().allMatch(Objects::isNull) && lowerBound.isEmpty() && higherBound.isEmpty()) {
            offers.addAll(offerService.findAll());
        }

        Tuple2<List<Offer>, List<Integer>> tuple = preprocess(offers, 0, userDTO);
        model.addAttribute("offersTab", tuple.getFirst());
        model.addAttribute("tabsLen", tuple.getSecond());
        model.addAttribute("adminUser", userDTO.isAdmin());
        modelAndView.addObject("offerSearch", new SearchDTO());
        modelAndView.setViewName("landing");
        return modelAndView;
    }
}