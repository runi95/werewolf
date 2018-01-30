package com.werewolf.gameplay.rules;

import com.werewolf.Messages.PlayerMessage;

import java.util.List;

public interface DayRule extends Rule {

    List<PlayerMessage> dayStarted();

}
