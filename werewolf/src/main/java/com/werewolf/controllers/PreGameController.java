package com.werewolf.controllers;

import com.werewolf.components.JoinGameFormValidator;
import com.werewolf.components.JoinLobbyFormValidator;
import com.werewolf.data.JoinGameForm;
import com.werewolf.data.JoinLobbyForm;
import com.werewolf.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@PreAuthorize("isAuthenticated()")
@Controller
public class PreGameController {

    @Autowired
    JoinGameFormValidator joinGameFormValidation;

    @Autowired
    JoinLobbyFormValidator joinLobbyFormValidation;

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMainPage(Authentication auth) {
        if(!(auth instanceof AnonymousAuthenticationToken) && auth != null) {
            return "redirect:/home";
        }
        return "main";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView getHomePage() {
        return new ModelAndView("main", "joinGameForm", new JoinGameForm());
    }

    @RequestMapping(value = "/lobby", method = RequestMethod.GET)
    public String getLobby() {
        return "redirect:/home";
    }

    @RequestMapping(value = "/lobby", method = RequestMethod.POST)
    public String postLobby(@Valid @ModelAttribute("joinLobbyForm") JoinLobbyForm joinLobbyForm, BindingResult bindingResult, Model model) {
        // TODO: Make sure this process is failsafe!
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        joinLobbyForm.setUserid(accountService.findByUsername(name).getId());
        joinLobbyFormValidation.validate(joinLobbyForm, bindingResult);
        model.addAttribute("gamecode", joinLobbyForm.getGameid());
        return "lobby";
    }
}
