package com.werewolf.components;

import com.werewolf.data.JoinLobbyForm;
import com.werewolf.entities.GameEntity;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.services.AccountService;
import com.werewolf.services.JoinGameService;
import com.werewolf.services.JoinLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class JoinLobbyFormValidator implements Validator{

    @Autowired
    JoinLobbyService joinLobbyService;

    @Autowired
    JoinGameService joinGameService;

    @Override
    public boolean supports(Class<?> aClass) {
        return JoinLobbyForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        // Should in theory never be null... in theory!
        JoinLobbyForm joinLobbyForm = (JoinLobbyForm) o;

            // Check if there's an existing started game with the given ID
            if (joinGameService.gameidIsPresent(joinLobbyForm.getGameid())) {
                // Check if the player was already in the existing game,
                // he could've crashed / accidentally left
                // TODO: if(joinGameService.findByGameId(joinLobbyForm.getGameid()).getAlivePlayers().contains())
            } else {
                // No game with given ID exists, check lobby

                if (!joinLobbyService.gameidIsPresent(joinLobbyForm.getGameid())) {
                    // No game nor lobby with given ID exists, error!
                    errors.rejectValue("gameid", "No lobby with id " + joinLobbyForm.getGameid() + " exists!");
                }
            }
        }
}
