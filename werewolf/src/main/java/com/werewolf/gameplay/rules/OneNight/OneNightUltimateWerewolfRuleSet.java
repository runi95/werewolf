package com.werewolf.gameplay.rules.OneNight;

import com.werewolf.gameplay.RuleModel;
import com.werewolf.gameplay.rules.*;

public class OneNightUltimateWerewolfRuleSet implements RuleSet {

    public static final String NAME = "One Night Ultimate Werewolf";
    public static final int MIN_PLAYER_AMOUNT = 1, MAX_PLAYER_AMOUNT = 20;

    private ActionRule[] actionRules = new ActionRule[] {};
    private ChatRule[] chatRules = new ChatRule[] { new ChatRuleCanOnlyChatDuringDayAndLobbyPhase() };
    private DayRule[] dayRules = new DayRule[] { new DayRuleEveryoneCanVoteOnAlivePlayersExceptSelf() };
    private GamePhaseRule[] gamePhaseRules = new GamePhaseRule[] {};
    private LobbyInitializationRule[] lobbyInitializationRules = new LobbyInitializationRule[] { new OneNightInitializationRule() };
    private NightRule[] nightRules = new NightRule[] { new OneNightNightRule() };
    private PlayerInitializationRule[] playerInitializationRules = new PlayerInitializationRule[] { new OneNightInitializationRule() };
    private VoteRule[] voteRules = new VoteRule[] { new VoteRuleEndsGame() };
    private WinConditionRule[] winConditionRules = new WinConditionRule[] {};

    public String getName() {
        return NAME;
    }

    public int getMinPlayerAmount() {
        return MIN_PLAYER_AMOUNT;
    }

    public int getMaxPlayerAmount() {
        return MAX_PLAYER_AMOUNT;
    }

    public RuleModel getRuleModel() {
        RuleModel ruleModel = new RuleModel();
        ruleModel.setMinPlayerAmount(1);
        ruleModel.setMaxPlayerAmount(20);
        ruleModel.setName(NAME);

        for(ActionRule r : actionRules)
            ruleModel.addActionRule(r);

        for(ChatRule r : chatRules)
            ruleModel.addChatRule(r);

        for(DayRule r : dayRules)
            ruleModel.addDayRule(r);

        for(GamePhaseRule r : gamePhaseRules)
            ruleModel.addGamePhaseRule(r);

        for(LobbyInitializationRule r : lobbyInitializationRules)
            ruleModel.addLobbyInitializationRule(r);

        for(NightRule r : nightRules)
            ruleModel.addNightRule(r);

        for(PlayerInitializationRule r : playerInitializationRules)
            ruleModel.addPlayerInitializationRule(r);

        for(VoteRule r : voteRules)
            ruleModel.addVoteRule(r);

        for(WinConditionRule r : winConditionRules)
            ruleModel.addWinConditionRule(r);

        return ruleModel;
    }
}
