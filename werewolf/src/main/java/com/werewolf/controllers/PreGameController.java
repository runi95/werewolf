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

    @Autowired
    JoinLobbyFormValidator joinLobbyFormValidation;

    @Autowired
    AccountService accountService;

    @Autowired
    JoinLobbyService joinLobbyService;

    @GetMapping(value = "/")
    public String getMainPage(Authentication auth, Principal principal, Device device) {
        if(device.isMobile()) {
            return "main-mobile";
        } else if(device.isTablet()) {
            return "main";
        } else {
            return "main";
        }
    }
}
