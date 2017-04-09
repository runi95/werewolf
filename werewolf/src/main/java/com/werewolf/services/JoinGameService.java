package com.werewolf.services;

import com.werewolf.data.JoinGameForm;
import com.werewolf.entities.LobbyEntity;

public interface JoinGameService {

    void create(JoinGameForm joinGameForm);

    LobbyEntity findByGameId(String gameId);
    LobbyEntity findById(long id);

    //void update(LobbyEntity lobbyEntity, JoinGameForm joinGameForm);

    /**
     * Get an edit form DTO for the given user
     * @param lobbyEntity the GameEntity to generate edit form from
     * @return editform for the given user
     */
    JoinGameForm getEditForm(LobbyEntity lobbyEntity);
}
