package com.werewolf.gameplay.roles;

import com.werewolf.gameplay.ChaoticGood;

public class Amnesiac implements ChaoticGood {
	private final String name = "Amnesiac";
	private final String description = "As an amnesiac you're unable to remember your true role until day 3";
	
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
