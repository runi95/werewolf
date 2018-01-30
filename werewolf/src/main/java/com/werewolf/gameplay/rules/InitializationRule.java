package com.werewolf.gameplay.rules;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.entities.LobbyEntity;

import java.util.List;

public interface InitializationRule extends Rule {

    List<PlayerMessage> initialize(LobbyEntity lobbyEntity);

}
