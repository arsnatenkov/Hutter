package application.controller;

import application.entity.Offer;
import application.entity.User;
import application.service.OfferService;
import application.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
public class OfferController {
    OfferService offerService;
    UserService userService;

    @PostMapping(value = "/addOffer")
    public ModelAndView addOffer(@Valid Offer offer, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        List<Offer> offerExists = offerService.findByAddress(offer.getAddress());

        if (offerExists != null && offerExists.contains(offer)) {
            bindingResult
                    .rejectValue("address", "error.offer",
                            "There is already an offer registered with same offer address provided");
        }
        if (!bindingResult.hasErrors()) {
            offerService.saveOffer(offer);
            modelAndView.addObject("successMessage", "Offer successfully added");
            modelAndView.addObject("offer", new Offer());
        }

        modelAndView.setViewName("/addOffer");
        return modelAndView;
    }

    @GetMapping(value = "/offer")
    public ModelAndView offer(HttpServletRequest request) {
        String id = request.getParameter("id");
        Offer offer = offerService.findByPublicId(Integer.parseInt(id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        ModelAndView modelAndView = new ModelAndView();
        StringBuilder sb = new StringBuilder();

        sb.append(guestUI(offer));

        if (user.getActive() && user.getId().equals(offer.getHostId()))
            sb.append(hostUI(offer));

        modelAndView.addObject("offerDisplay", sb.toString());
        modelAndView.setViewName("offer");
        return modelAndView;
    }

    private String hostUI(Offer offer) {
        return "<div class=\"hostUI\"><a href=/edit?id=\"" + offer.getPublicId() + "\">Изменить</a></div>";
    }

    private String guestUI(Offer offer) {
        String title = offer.getAddress() + ", " + offer.getTotalArea() + "м²";
        String body = offer.shortDescription() + offer.getDescription() + "<br/>";
        return "<h2>" + title + "</h2>" + body;
    }
}
