package com.werewolf.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.werewolf.data.AccountRegisterForm;
import com.werewolf.services.AccountService;


/**
 * Validator used to validate a UserRegisterForm before we pass it down to the
 * service.
 */
@Component
public class AccountRegisterFormValidation implements Validator {

	@Autowired
	private AccountService userService;

	@Override
	public boolean supports(Class<?> aClass) {
		return AccountRegisterForm.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		AccountRegisterForm userRegisterForm = (AccountRegisterForm) o;

		try {
			userService.findByUsername(userRegisterForm.getUsername());
			errors.rejectValue("username", "AccountRegisterForm.username.alreadyexists");
		} catch (IllegalArgumentException e) {

		}

		if (!userRegisterForm.getPassword().equals(userRegisterForm.getPasswordRepeated())) {
			errors.rejectValue("passwordRepeated", "UserRegisterForm.password.doesnotmatch");
		}
	}

}
