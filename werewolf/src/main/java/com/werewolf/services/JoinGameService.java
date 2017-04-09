package com.werewolf.services;

import com.werewolf.data.JoinGameForm;
import com.werewolf.entities.GameEntity;

public interface JoinGameService {

    void create(JoinGameForm joinGameForm);

    GameEntity findByGameId(String gameId);
    GameEntity findById(long id);

    //void update(GameEntity gameEntity, JoinGameForm joinGameForm);

    /**
     * Get an edit form DTO for the given user
     * @param gameEntity the GameEntity to generate edit form from
     * @return editform for the given user
     */
    JoinGameForm getEditForm(GameEntity gameEntity);
}
