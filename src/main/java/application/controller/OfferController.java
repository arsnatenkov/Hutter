package application.controller;

import application.entity.Favourite;
import application.entity.Offer;
import application.entity.User;
import application.service.FavouriteService;
import application.service.OfferService;
import application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
        Offer offer = offerService.findById(Integer.parseInt(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView modelAndView = new ModelAndView();

        User user = userService.findUserByUserName(auth.getName());
        boolean isAuth = user != null;
        if (isAuth && user.getId().equals(offer.getHostId()))
            model.addAttribute("host", true);
        else
            model.addAttribute("host", false);

        model.addAttribute("offerDisplay", offerService.findById(Integer.parseInt(id)));
        modelAndView.setViewName("offer");
        return modelAndView;
    }

    @GetMapping(value = "/create")
    public ModelAndView addOffer() {
        ModelAndView modelAndView = new ModelAndView();
        Offer offer = new Offer();
        modelAndView.addObject("offer", offer);
        modelAndView.setViewName("create");
        return modelAndView;
    }

    @PostMapping(value = "/create")
    public String createNewOffer(@Valid Offer offer, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        List<Offer> offerExists = offerService.findByAddress(offer.getAddress());

        if (offerExists != null && offerExists.contains(offer)) {
            bindingResult
                    .rejectValue("address", "error.offer",
                            "There is already an offer registered with same offer address provided");
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

    @GetMapping(value = "/save/{offerId}")
    public String saveOffer(@PathVariable("offerId") Integer offerId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        favouriteService.saveFavourite(new Favourite(user.getId(), offerId,
                offerService.findById(offerId).getAddress()));
        return "redirect:/offer?id=" + offerId;
    }
}
