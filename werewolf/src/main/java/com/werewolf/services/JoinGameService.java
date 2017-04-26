package com.werewolf.services;

import com.werewolf.data.JoinGameForm;
import com.werewolf.entities.GameEntity;
import com.werewolf.entities.LobbyEntity;

public interface JoinGameService {

    void create(LobbyEntity lobbyEntity);

    GameEntity findByGameId(String gameId);
    GameEntity findById(long id);

    boolean gameidIsPresent(String gameid);

    //void update(GameEntity gameEntity, JoinGameForm joinGameForm);

    /**
     * Get an edit form DTO for the given user
     * @param gameEntity the GameEntity to generate edit form from
     * @return editform for the given user
     */
    JoinGameForm getEditForm(GameEntity gameEntity);
}
