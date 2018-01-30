package com.werewolf.gameplay;

import com.werewolf.gameplay.rules.*;

import java.util.ArrayList;

public class RuleModel {

    ArrayList<ActionRule> actionRules;
    ArrayList<ChatRule> chatRules;
    ArrayList<DayRule> dayRules;
    ArrayList<GamePhaseRule> gamePhaseRules;
    ArrayList<InitializationRule> initializationRules;
    ArrayList<NightRule> nightRules;
    ArrayList<VoteRule> voteRules;
    ArrayList<WinConditionRule> winConditionRules;

    public void addActionRule(ActionRule rule) { actionRules.add(rule); }
    public void addChatRule(ChatRule rule) { chatRules.add(rule); }
    public void addDayRule(DayRule rule) { dayRules.add(rule); }
    public void addGamePhaseRule(GamePhaseRule rule) { gamePhaseRules.add(rule); }
    public void addInitializationRule(InitializationRule rule) { initializationRules.add(rule); }
    public void addNightRule(NightRule rule) { nightRules.add(rule); }
    public void addWinConditionRule(WinConditionRule rule) { winConditionRules.add(rule); }

    public ArrayList<ActionRule> getActionRules() { return actionRules; }
    public ArrayList<ChatRule> getChatRules() { return chatRules; }
    public ArrayList<DayRule> getDayRules() { return dayRules; }
    public ArrayList<GamePhaseRule> getGamePhaseRules() { return gamePhaseRules; }
    public ArrayList<InitializationRule> getInitializationRules() { return initializationRules; }
    public ArrayList<NightRule> getNightRules() { return nightRules; }
    public ArrayList<VoteRule> getVoteRules() { return voteRules; }
    public ArrayList<WinConditionRule> getWinConditionRules() { return winConditionRules; }

}
