package com.werewolf.components;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.werewolf.data.JoinGameForm;

@Component
public class JoinGameFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> aClass) {
		return JoinGameForm.class.equals(aClass);
	}
	
	@Override
	public void validate(Object o, Errors errors) {
//		JoinGameFormValidator joinGameForm = (JoinGameFormValidator) o;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gameId",
			"required.gameId", "Game ID is required.");
	}
	
}
