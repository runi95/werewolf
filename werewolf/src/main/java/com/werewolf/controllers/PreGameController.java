package com.werewolf.controllers;

import com.werewolf.components.JoinLobbyFormValidator;
import com.werewolf.services.AccountService;
import com.werewolf.services.JoinLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Optional;

@PreAuthorize("isAuthenticated()")
@Controller
public class PreGameController {

    @Autowired
    JoinLobbyFormValidator joinLobbyFormValidation;

    @Autowired
    AccountService accountService;

    @Autowired
    JoinLobbyService joinLobbyService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getMainPage(Authentication auth, Principal principal) {
        if(!(auth instanceof AnonymousAuthenticationToken) && auth != null) {
            return new ModelAndView("redirect:/home");
        }
        return new ModelAndView("main", "username", principal.getName());
    }

    @RequestMapping(value = "/home")
    public ModelAndView getHomePage(@RequestParam Optional<String> error, Principal principal) {
        return new ModelAndView("main", "username", principal.getName());
    }

    @RequestMapping(value = "/lobby", method = RequestMethod.GET)
    public String getLobby() {
        return "redirect:/home";
    }

}
