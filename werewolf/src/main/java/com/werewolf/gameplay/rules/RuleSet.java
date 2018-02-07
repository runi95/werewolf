package com.werewolf.gameplay.rules;

public interface RuleSet {

    /**
     * @return name of the rule set
     */
    String getName();

    Rule[] getRules();

}
