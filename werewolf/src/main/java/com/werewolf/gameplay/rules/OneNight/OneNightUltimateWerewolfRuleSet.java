package com.werewolf.gameplay.rules.OneNight;

import com.werewolf.gameplay.rules.Rule;
import com.werewolf.gameplay.rules.RuleSet;

public class OneNightUltimateWerewolfRuleSet implements RuleSet {

    public static final String NAME = "One Night Ultimate Werewolf";
    Rule[] rules = new Rule[] { new VoteRuleEndsGame() };

    public String getName() {
        return NAME;
    }

    public Rule[] getRules() {
        return rules;
    }

}
