package application.controller;

import application.dto.SearchDTO;
import application.entity.Favourite;
import application.entity.Offer;
import application.entity.User;
import application.service.FavouriteService;
import application.service.OfferService;
import application.service.UserService;
import groovy.lang.Tuple2;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OfferController {
    @Autowired
    OfferService offerService;
    @Autowired
    UserService userService;
    @Autowired
    FavouriteService favouriteService;

    @GetMapping(value = "/offer")
    public ModelAndView offer(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        Optional<Offer> offer = offerService.findById(Long.parseLong(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findUserByUserName(auth.getName());

        model.addAttribute("host", user != null && user.getId().equals(offer.get().getHostId()));
        model.addAttribute("offerDisplay", offerService.findById(Long.parseLong(id)).get());
        modelAndView.setViewName("offer");
        return modelAndView;
    }

    @GetMapping(value = "/create")
    public ModelAndView addOffer() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("offer", new Offer());
        modelAndView.setViewName("create");
        return modelAndView;
    }

    @PostMapping(value = "/create")
    public String createNewOffer(@Valid Offer offer, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        List<Offer> offerExists = offerService.findByAddress(offer.getAddress());
        String message = "There is already an offer registered with same offer address provided";

        if (offerExists != null && offerExists.contains(offer)) {
            bindingResult.rejectValue("address", "error.offer", message);
        }

        if (!bindingResult.hasErrors()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByUserName(auth.getName());
            offer.setHostId(user.getId());
            offerService.saveOffer(offer);
            modelAndView.addObject("successMessage", "Offer successfully added");
            modelAndView.addObject("offer", new Offer());
        }

        modelAndView.setViewName("create");
        return "redirect:/visitor/account";
    }

    @GetMapping(value = "/deleteSaved/{offerId}")
    public String deleteSavedOffer(@PathVariable("offerId") Long offerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        List<Favourite> favourites = favouriteService.findByUserId(user.getId());

        for (Favourite f : favourites) {
            if (f.getOfferId().equals(offerId))
                favouriteService.deleteFavourite(f);
        }
        return "redirect:/visitor/account";
    }

    @GetMapping(value = "/save/{offerId}")
    public String saveOffer(@PathVariable("offerId") Long offerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        String address = offerService.findById(offerId).get().getAddress();
        Favourite favourite = new Favourite(user.getId(), offerId, address);
        List<Favourite> favourites = favouriteService.findByUserId(user.getId());

        boolean checker = false;
        for (Favourite f : favourites)
            if (!checker && equals(f, favourite)) checker = true;

        if (!checker) favouriteService.saveFavourite(favourite);

        return "redirect:/offer?id=" + offerId;
    }

    private boolean equals(Favourite a, Favourite b) {
        return a.getUserId().equals(b.getUserId()) && a.getOfferId().equals(b.getOfferId());
    }
}