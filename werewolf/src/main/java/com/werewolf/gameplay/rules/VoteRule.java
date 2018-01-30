package com.werewolf.gameplay.rules;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;

import java.util.List;

public interface VoteRule extends Rule {

    List<PlayerMessage> vote(LobbyEntity lobbyEntity, LobbyPlayer voter, LobbyPlayer target, LobbyPlayer oldTarget, boolean flag);

}
