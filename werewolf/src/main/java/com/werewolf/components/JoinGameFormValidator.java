package com.werewolf.components;

import com.werewolf.entities.GameEntity;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.services.JoinGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.werewolf.data.JoinGameForm;

@Component
public class JoinGameFormValidator implements Validator {

	@Autowired
	JoinGameService joinGameService;

	@Override
	public boolean supports(Class<?> aClass) {
		return JoinGameForm.class.equals(aClass);
	}
	
	@Override
	public void validate(Object o, Errors errors) {
		// Should in theory never be null... in theory!
		JoinGameForm joinGameForm = (JoinGameForm) o;

		// TODO:
		// If a GameEntity with this gameID exists
		// then they should only be able to join
		// if they were already in the game and accidentally quit / left
		// If not then they should be given an error "Game with that ID already exists!"
		// If the game with given gameID does not exist then check if there's a lobby
		// with the given gameID, if there is then join the lobby.
		// If there is no game nor lobby with given gameID then create a new lobby!

		GameEntity gameEntity = null;
		try {
			gameEntity = joinGameService.findByGameId(joinGameForm.getGameId());
			//LobbyPlayer lobbyPlayer = new LobbyPlayer();
			//lobbyPlayer.setUser();
			//lobbyEntity.getPlayers().add(lobbyPlayer);
		} catch (IllegalArgumentException e) {

		}

		if(gameEntity == null) {
			gameEntity = new GameEntity();
		}

	}
	
}
