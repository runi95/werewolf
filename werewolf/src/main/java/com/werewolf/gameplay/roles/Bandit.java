package com.werewolf.gameplay.roles;

import com.werewolf.gameplay.Evil;

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

}
