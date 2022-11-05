package com.werewolf.controllers;


import com.werewolf.components.AccountRegisterFormValidation;
import com.werewolf.data.AccountRegisterForm;
import com.werewolf.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class PreAuthController {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRegisterFormValidation accountRegisterFormValidation;

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
    
    @GetMapping(value = "/test")
    public String test() {
    	return "test";
    }
}
