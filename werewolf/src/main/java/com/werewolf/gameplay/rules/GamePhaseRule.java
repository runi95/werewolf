package com.werewolf.gameplay.rules;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.entities.GamePhase;

import java.util.List;

public interface GamePhaseRule extends Rule {

    List<PlayerMessage> gamePhaseChanged(GamePhase oldGamePhase, GamePhase newGamePhase);

}
