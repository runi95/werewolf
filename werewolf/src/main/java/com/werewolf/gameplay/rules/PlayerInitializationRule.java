package com.werewolf.gameplay.rules;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;

import java.util.List;

public interface PlayerInitializationRule extends Rule {

    List<PlayerMessage> initializePlayer(LobbyEntity lobbyEntity, LobbyPlayer lobbyPlayer);

}
