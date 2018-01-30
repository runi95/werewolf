package com.werewolf.gameplay.rules.OneNight;

import com.werewolf.gameplay.rules.Rule;
import com.werewolf.gameplay.rules.RuleSet;

public class OneNightUltimateWerewolfRuleSet implements RuleSet {

    Rule[] rules = new Rule[] { new VoteRuleEndsGame() };

    public Rule[] getRules() {
        return rules;
    }

}
