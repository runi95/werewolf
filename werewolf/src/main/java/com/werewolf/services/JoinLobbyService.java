package com.werewolf.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.werewolf.data.JoinLobbyForm;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;

public interface JoinLobbyService {
	
	LobbyPlayer getPlayer(long userid);
    LobbyEntity create(JoinLobbyForm joinLobbyForm);
    LobbyEntity join(JoinLobbyForm joinLobbyForm);
    
    void leave(String username);
    void vote(String username, String voteon, boolean vote);
    void setReadyStatus(String username, boolean ready);
    
    void getPlayers(String username);
    void initializeGame(String username);
    void nightAction(String username, String target, boolean act);
    void getGamePhase(String username);
    void getRole(String username);
    
    LobbyEntity findByGameId(String gameId);

    boolean gameidIsPresent(String gameid);

    //void update(GameEntity gameEntity, JoinGameForm joinGameForm);

    /**
     * Get an edit form DTO for the given user
     * @param lobbyEntity the GameEntity to generate edit form from
     * @return editform for the given user
     */
    JoinLobbyForm getEditForm(LobbyEntity lobbyEntity);
    
    public static String convertObjectToJson(Object message) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		String arrayToJson = null;
		try {
			arrayToJson = objectMapper.writeValueAsString(message);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return arrayToJson;
	}
}
