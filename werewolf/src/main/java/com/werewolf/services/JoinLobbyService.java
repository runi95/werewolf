package com.werewolf.services;

import com.werewolf.data.JoinLobbyForm;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;

public interface JoinLobbyService {

	void dropTable();
	
    LobbyEntity create(JoinLobbyForm joinLobbyForm);
    LobbyEntity join(JoinLobbyForm joinLobbyForm);
    void leave(LobbyPlayer lobbyPlayer);
    void loadGame(LobbyEntity lobbyEntity);
    int vote(LobbyPlayer voter, String voteon);
    int removeVote(LobbyPlayer voter, String voteon);
    Integer setReadyStatus(LobbyPlayer lobbyPlayer, boolean ready);
    Integer getPlayerCount(LobbyPlayer lobbyPlayer);
    
    LobbyEntity findByGameId(String gameId);
    LobbyEntity findById(long id);

    boolean gameidIsPresent(String gameid);

    //void update(GameEntity gameEntity, JoinGameForm joinGameForm);

    /**
     * Get an edit form DTO for the given user
     * @param lobbyEntity the GameEntity to generate edit form from
     * @return editform for the given user
     */
    JoinLobbyForm getEditForm(LobbyEntity lobbyEntity);
}
