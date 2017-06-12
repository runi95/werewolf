package com.werewolf.components;

import com.werewolf.data.AccountRegisterForm;
import com.werewolf.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class AccountEditFormValidation implements Validator {
	
	@Autowired
	AccountService accountService;
	
	@Override
    public boolean supports(Class<?> aClass) {
        return AccountRegisterForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
    	AccountRegisterForm accountRegisterForm = (AccountRegisterForm) o;

        try {
        	accountService.findByUsername(accountRegisterForm.getUsername());
            errors.rejectValue("username", "AccountRegisterForm.username.alreadyexists");
        } catch (IllegalArgumentException e) {

        }

        if(!accountRegisterForm.getPassword().equals(accountRegisterForm.getPasswordRepeated())) {
            errors.rejectValue("passwordRepeated", "UserRegisterForm.password.doesnotmatch");
        }
    }
}
