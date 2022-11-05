package com.werewolf.gameplay.rules;

import com.werewolf.gameplay.RuleModel;

public interface RuleSet {

    /**
     * Returns the name of this rule set.
     * @return name as string
     */
    String getName();

    RuleModel getRuleModel();

    /**
     * Returns the minimum number of players required to start a game.
     * @return int
     */
    int getMinPlayerAmount();

    /**
     * Returns the maximum number of players a lobby can hold.
     * @return int
     */
    int getMaxPlayerAmount();

}
