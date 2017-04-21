package com.werewolf.controllers;

import com.werewolf.components.JoinLobbyFormValidator;
import com.werewolf.data.JoinLobbyForm;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.User;
import com.werewolf.services.AccountService;
import com.werewolf.services.JoinLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import javax.validation.Valid;

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
    public String getMainPage(Authentication auth) {
        if(!(auth instanceof AnonymousAuthenticationToken) && auth != null) {
            return "redirect:/home";
        }
        return "main";
    }

    @RequestMapping(value = "/home")
    public String getHomePage(@RequestParam Optional<String> error) {
        return "main";
    }

    @RequestMapping(value = "/lobby", method = RequestMethod.GET)
    public String getLobby() {
        return "redirect:/home";
    }

    @RequestMapping(value = "/lobby", method = RequestMethod.POST)
    public ModelAndView postLobby(@Valid @ModelAttribute("joinLobbyForm") JoinLobbyForm joinLobbyForm, BindingResult bindingResult) {
        // TODO: Make sure this process is failsafe!
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); // Get logged in username
        User user = accountService.findByUsername(name); // Get logged in User
        joinLobbyForm.setUser(user);
        joinLobbyForm.setUserid(user.getId()); // Might be deprecated, but has been kept
        joinLobbyForm.setGameid(joinLobbyForm.getGameid().toUpperCase());

        LobbyEntity lobbyEntity = null;

        if(joinLobbyForm.getGameid().equals("")) {
            lobbyEntity = joinLobbyService.create(joinLobbyForm); // No game id means nothing to validate
        } else {
            joinLobbyFormValidation.validate(joinLobbyForm, bindingResult);
            if(bindingResult.hasErrors())
            	return new ModelAndView("main", "error", "error joining lobby, please check the Game ID again");
            else
            	lobbyEntity = joinLobbyService.join(joinLobbyForm);
        }

        return new ModelAndView("lobby", "gamecode", lobbyEntity.getGameId());
    }

}
