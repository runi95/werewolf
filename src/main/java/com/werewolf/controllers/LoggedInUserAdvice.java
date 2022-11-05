package com.werewolf.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Controlleradvice so that we can access the @LoggedInUser from views
 */
@ControllerAdvice
public class LoggedInUserAdvice {

	@ModelAttribute("loggedInUser")
	public UserDetails getLoggedInUser(Authentication authentication) {
		return (authentication == null) ? null : (UserDetails) authentication.getPrincipal();
	}

}
