package com.werewolf.gameplay.rules;

public interface WinConditionRule extends Rule {

    boolean checkWinCondition();

    String[] getWinners();

}
