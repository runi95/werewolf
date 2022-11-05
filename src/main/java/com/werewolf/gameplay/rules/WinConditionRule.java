package com.werewolf.gameplay.rules;

import com.werewolf.entities.LobbyEntity;

public interface WinConditionRule extends Rule {

    boolean checkWinCondition(LobbyEntity lobbyEntity);

    String[] getWinners(LobbyEntity lobbyEntity);

}
