package com.werewolf.services;

import java.util.List;

import com.werewolf.Messages.LobbyMessage;
import com.werewolf.data.JoinLobbyForm;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;

public interface JoinLobbyService {
	
	LobbyPlayer getPlayer(long userid);

    LobbyEntity create(JoinLobbyForm joinLobbyForm);
    LobbyEntity join(JoinLobbyForm joinLobbyForm);
    List<LobbyMessage> join(String username);
    List<LobbyMessage> leave(String username);
    List<LobbyMessage> leave(LobbyPlayer lobbyPlayer);
    List<LobbyMessage> vote(String username, String voteon, boolean vote);
    List<LobbyMessage> setReadyStatus(String username, boolean ready);
    List<LobbyMessage> getPlayers(String username);
    List<LobbyMessage> gameRequest(String username);
    List<LobbyMessage> initializeGame(String username);
    
    LobbyEntity findByGameId(String gameId);

    boolean gameidIsPresent(String gameid);

    //void update(GameEntity gameEntity, JoinGameForm joinGameForm);

    /**
     * Get an edit form DTO for the given user
     * @param lobbyEntity the GameEntity to generate edit form from
     * @return editform for the given user
     */
    JoinLobbyForm getEditForm(LobbyEntity lobbyEntity);
}
