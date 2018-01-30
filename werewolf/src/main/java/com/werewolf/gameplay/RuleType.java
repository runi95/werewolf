package com.werewolf.gameplay;

public enum RuleType {

    /**
     * The Initialization rule is ran upon initialization of the game.
     * This type of rule should always start a new phase depending on
     * how you want the game to start.
     */
    InitializationRule,

    /**
     * The Game Phase rule is ran whenever the game phase changes from
     * night to day or vice versa.
     * This type of rule should always activate everyone's night action when
     * going from night to day.
     */
    GamePhaseRule,

    /**
     * The Day rule is the same as Game Phase rule, but only runs when a
     * new in-game day starts.
     */
    DayRule,

    /**
     * The Day rule is the same as Game Phase rule, but only runs when a
     * new in-game night starts.
     */
    NightRule,

    /**
     * The Action rule is ran whenever someone selects a target for
     * their night action.
     * This type of rule should always check whether or not the actor
     * has already selected another target or not.
     */
    ActionRule,

    /**
     * The Vote rule is ran whenever someone is voting to lynch.
     * This type of rule should always check whether or not the voting
     * person already has a vote elsewhere and whether or not the
     * voted person has enough votes to be lynched
     */
    VoteRule,

    /**
     * The Win Condition rule is ran after the Game Phase rule at day and
     * night to check whether the game should end or continue.
     * This type of rule should return a list of winners, if the list is
     * empty it means the game continues.
     */
    WinConditionRule,

    /**
     * The Chat rule is ran whenever someone sends a chat message.
     * This type of rule should carry out the message to everyone.
     */
    ChatRule
}
