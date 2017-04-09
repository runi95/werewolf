package com.werewolf.services;

import com.werewolf.data.JoinLobbyForm;
import com.werewolf.entities.LobbyEntity;

public interface JoinLobbyService {

    void create(JoinLobbyForm joinLobbyForm);

    LobbyEntity findByGameId(String gameId);
    LobbyEntity findById(long id);

    //void update(GameEntity gameEntity, JoinGameForm joinGameForm);

    /**
     * Get an edit form DTO for the given user
     * @param lobbyEntity the GameEntity to generate edit form from
     * @return editform for the given user
     */
    JoinLobbyForm getEditForm(LobbyEntity lobbyEntity);
}
