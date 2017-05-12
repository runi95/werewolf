package com.werewolf.gameplay.roles;

import com.werewolf.gameplay.EmulationCharacter;
import com.werewolf.gameplay.Evil;
import com.werewolf.gameplay.GameEmulator;

public class Bandit implements Evil {
	private final String name = "Bandit";
	private final String description = "You're a bandit, a simple follower of the boss, you can head out at night to kill someone. If the boss dies you'll automatically be promoted";
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getAlignment() {
		return alignment;
	}

	@Override
	public String getGoal() {
		return goal;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public String getInquestMessage() {
		return "Your target seems familiar with weapons. (Bandit, Guard)";
	}

	@Override
	public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
		game.voteEvilKill(self, target);
	}

}
