package com.werewolf.gameplay.rules;

import com.werewolf.Messages.PlayerMessage;

import java.util.List;

public interface NightRule extends Rule {

    List<PlayerMessage> nightStarted();

}
