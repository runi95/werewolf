package com.werewolf.components;

import com.werewolf.data.JoinLobbyForm;
import com.werewolf.services.JoinLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class JoinLobbyFormValidator implements Validator{

    @Autowired
    JoinLobbyService joinLobbyService;

    @Override
    public boolean supports(Class<?> aClass) {
        return JoinLobbyForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        // Should in theory never be null... in theory!
        JoinLobbyForm joinLobbyForm = (JoinLobbyForm) o;
        
        }
}
