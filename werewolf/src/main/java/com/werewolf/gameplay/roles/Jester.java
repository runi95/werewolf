package com.werewolf.gameplay.roles;

import com.werewolf.gameplay.Neutral;

public class Jester implements Neutral {
	private final String name = "Jester";
	private final String description = "";
	
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
