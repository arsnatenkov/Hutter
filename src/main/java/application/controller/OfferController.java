package application.controller;

import application.entity.Offer;
import application.entity.User;
import application.service.OfferService;
import application.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

@Controller
public class OfferController {
    OfferService offerService;
    UserService userService;
    @GetMapping(value = "/offer")
    public ModelAndView offer(HttpServletRequest request) {
        String id = request.getParameter("id");
        Offer offer = offerService.findByPublicId(Integer.parseInt(id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("offer");
        if(user.getActive() && user.getId().equals(offer.getHostId())){
//            ModelAndView modelAndView = new ModelAndView();
//            modelAndView.setViewName("offer");
            return modelAndView;
        }
        return modelAndView;
    }
}
