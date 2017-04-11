package com.werewolf.controllers;

import java.util.Optional;

import javax.validation.Valid;

import com.werewolf.components.JoinLobbyFormValidator;
import com.werewolf.data.JoinLobbyForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.werewolf.components.AccountRegisterFormValidation;
import com.werewolf.components.JoinGameFormValidator;
import com.werewolf.data.AccountRegisterForm;
import com.werewolf.data.JoinGameForm;
import com.werewolf.services.AccountService;

@Controller
public class MainPageController {

	@Autowired
	AccountService accountService;
	
	@Autowired
    AccountRegisterFormValidation accountRegisterFormValidation;
	
	@Autowired
	JoinGameFormValidator joinGameFormValidation;

	@Autowired
    JoinLobbyFormValidator joinLobbyFormValidation;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@PreAuthorize("isAuthenticated()")
    public String getMainPage(Authentication auth) {
        if(!(auth instanceof AnonymousAuthenticationToken) && auth != null) {
            return "redirect:/home";
        }
        return "main";
    }
    
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ModelAndView getHomePage() {
    	return new ModelAndView("main", "joinGameForm", new JoinGameForm());
    }
    
    @RequestMapping(value = "/home", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public String postHomePage(@Valid @ModelAttribute("joinLobbyForm") JoinLobbyForm joinLobbyForm, BindingResult bindingResult) {
    	joinLobbyFormValidation.validate(joinLobbyForm, bindingResult);
    	
    	if(bindingResult.hasErrors()) {
    		System.out.println(bindingResult.getAllErrors());
    		return "main";
    	}
    	return "lobby";
    }

    @RequestMapping(value = "/lobby", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public String getLobby() {
	    return "redirect:/home";
    }

    @RequestMapping(value = "/lobby", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public String postLobby(@Valid @ModelAttribute("joinLobbyForm") JoinLobbyForm joinLobbyForm, BindingResult bindingResult, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        joinLobbyFormValidation.validate(joinLobbyForm, bindingResult);
        String name = auth.getName(); //get logged in username
        model.addAttribute("gamecode",joinLobbyForm.getGameid());
	    return "lobby";
    }

    @GetMapping(value = "/login")
    public ModelAndView getLoginPage(@RequestParam Optional<String> error) {
    	return new ModelAndView("login", "error", error);
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView getRegister() {
        return new ModelAndView("register", "accountRegisterForm", new AccountRegisterForm());
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String postRegister(@ModelAttribute("accountRegisterForm") AccountRegisterForm accountRegisterForm, BindingResult bindingResult) {
        accountRegisterFormValidation.validate(accountRegisterForm, bindingResult);

        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "register";
        }

        accountService.create(accountRegisterForm);

        return "redirect:/";
    }

}
