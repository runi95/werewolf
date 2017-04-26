package com.werewolf.gameplay.roles;

import com.werewolf.gameplay.NeutralEvil;

public class Heretic implements NeutralEvil {
	private final String name = "Heretic";
	public final String description = "You're a heretic, you're against the religious and have the ability to counter the priest's healing ability, you should try to side with other evildoers to survive";
	
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
