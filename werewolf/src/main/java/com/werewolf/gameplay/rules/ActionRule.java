package com.werewolf.gameplay.rules;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;

import java.util.List;

public interface ActionRule extends Rule{

    List<PlayerMessage> nightAction(LobbyEntity lobbyEntity, LobbyPlayer actor, LobbyPlayer target, LobbyPlayer oldTarget, boolean flag);

}
