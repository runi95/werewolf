package com.werewolf.gameplay.rules;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.entities.LobbyEntity;

import java.util.List;

public interface DayRule extends Rule {

    List<PlayerMessage> dayStarted(LobbyEntity lobbyEntity);

}
