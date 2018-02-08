package com.werewolf.gameplay;

import com.werewolf.gameplay.rules.*;

import java.util.ArrayList;

public class RuleModel {

    private ArrayList<ActionRule> actionRules = new ArrayList<>();
    private ArrayList<ChatRule> chatRules = new ArrayList<>();
    private ArrayList<DayRule> dayRules = new ArrayList<>();
    private ArrayList<GamePhaseRule> gamePhaseRules = new ArrayList<>();
    private ArrayList<InitializationRule> initializationRules = new ArrayList<>();
    private ArrayList<NightRule> nightRules = new ArrayList<>();
    private ArrayList<VoteRule> voteRules = new ArrayList<>();
    private ArrayList<WinConditionRule> winConditionRules = new ArrayList<>();
    private int minPlayerAmount = 4;
    private int maxPlayerAmount = 10;
    private String name;

    public void addActionRule(ActionRule rule) { actionRules.add(rule); }
    public void addChatRule(ChatRule rule) { chatRules.add(rule); }
    public void addDayRule(DayRule rule) { dayRules.add(rule); }
    public void addGamePhaseRule(GamePhaseRule rule) { gamePhaseRules.add(rule); }
    public void addInitializationRule(InitializationRule rule) { initializationRules.add(rule); }
    public void addNightRule(NightRule rule) { nightRules.add(rule); }
    public void addVoteRule(VoteRule rule) { voteRules.add(rule); }
    public void addWinConditionRule(WinConditionRule rule) { winConditionRules.add(rule); }
    public void setMinPlayerAmount(int minPlayerAmount) { this.minPlayerAmount = minPlayerAmount; }
    public void setMaxPlayerAmount(int maxPlayerAmount) { this.maxPlayerAmount = maxPlayerAmount;}
    public void setName(String name) { this.name = name; }

    public ArrayList<ActionRule> getActionRules() { return actionRules; }
    public ArrayList<ChatRule> getChatRules() { return chatRules; }
    public ArrayList<DayRule> getDayRules() { return dayRules; }
    public ArrayList<GamePhaseRule> getGamePhaseRules() { return gamePhaseRules; }
    public ArrayList<InitializationRule> getInitializationRules() { return initializationRules; }
    public ArrayList<NightRule> getNightRules() { return nightRules; }
    public ArrayList<VoteRule> getVoteRules() { return voteRules; }
    public ArrayList<WinConditionRule> getWinConditionRules() { return winConditionRules; }
    public int getMinPlayerAmount() { return minPlayerAmount; }
    public int getMaxPlayerAmount() { return maxPlayerAmount; }
    public String getName() { return name; }

}
