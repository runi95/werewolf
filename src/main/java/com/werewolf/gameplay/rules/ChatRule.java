package com.werewolf.gameplay.rules;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;

import java.util.List;

public interface ChatRule extends Rule {

    List<PlayerMessage> chat(LobbyEntity lobbyEntity, LobbyPlayer chatSourcePlayer, String message);

}
