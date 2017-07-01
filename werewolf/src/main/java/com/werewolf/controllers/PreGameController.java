package com.werewolf.controllers;

import com.werewolf.components.JoinLobbyFormValidator;
import com.werewolf.services.AccountService;
import com.werewolf.services.JoinLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Optional;

@PreAuthorize("isAuthenticated()")
@Controller
public class PreGameController {

    public static final String gameplaytext = "The game is played in cycles of day and night that can vary on the game mode you're playing, at night every player gets to perform their night action which is <em>usually</em> their strongest ability. During the day phase everyone can vote on whom to lynch and if anyone at any time get above 50% of the votes they'll get executed and have their role revealed. The evil roles are encouraged to lie and manipulate during this phase as telling them your true identity will most definitely get you lynched by the good town's people.";

    @Autowired
    JoinLobbyFormValidator joinLobbyFormValidation;

    @Autowired
    AccountService accountService;

    @Autowired
    JoinLobbyService joinLobbyService;

    @GetMapping(value = "/")
    public ModelAndView getMainPage(Authentication auth, Principal principal, Device device) {
        if(device.isMobile()) {
            return new ModelAndView("main-mobile", "gameplaytext", gameplaytext);
        } else if(device.isTablet()) {
            return new ModelAndView("main", "gameplaytext", gameplaytext);
        } else {
            return new ModelAndView("main", "gameplaytext", gameplaytext);
        }
    }
}
