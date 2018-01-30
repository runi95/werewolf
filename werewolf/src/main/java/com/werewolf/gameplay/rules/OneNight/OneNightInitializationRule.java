package com.werewolf.gameplay.rules.OneNight;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.gameplay.rules.InitializationRule;

import java.util.List;

public class OneNightInitializationRule implements InitializationRule {

    public List<PlayerMessage> initialize(LobbyEntity lobbyEntity) {
        lobbyEntity.setStartedState(true);

        return null;
    }

}
