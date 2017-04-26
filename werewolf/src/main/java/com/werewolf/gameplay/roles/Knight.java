package com.werewolf.gameplay.roles;

import com.werewolf.gameplay.Good;

public class Knight implements Good {
	private final String name = "Knight";
	private final String description = "As a knight you can decide to get up at night and kill someone, beware that killing a member of the town will get you executed the next day";
	
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
