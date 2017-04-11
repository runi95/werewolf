package com.werewolf.components;

import com.werewolf.data.JoinLobbyForm;
import com.werewolf.entities.LobbyEntity;
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

        // TODO:
        // If a GameEntity with this gameID exists
        // then they should only be able to join
        // if they were already in the game and accidentally quit / left
        // If not then they should be given an error "Game with that ID already exists!"
        // If the game with given gameID does not exist then check if there's a lobby
        // with the given gameID, if there is then join the lobby.
        // If there is no game nor lobby with given gameID then create a new lobby!

        LobbyEntity lobbyEntity = null;
        try {
            lobbyEntity = joinLobbyService.findByGameId(joinLobbyForm.getGameid());
            //LobbyPlayer lobbyPlayer = new LobbyPlayer();
            //lobbyPlayer.setUser();
            //lobbyEntity.getPlayers().add(lobbyPlayer);
        } catch (IllegalArgumentException e) {

        }

        if(lobbyEntity == null) {
            lobbyEntity = new LobbyEntity();
        }

    }
}
