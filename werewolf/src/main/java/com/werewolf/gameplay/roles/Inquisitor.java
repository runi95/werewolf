package com.werewolf.gameplay.roles;

import com.werewolf.gameplay.Good;

public class Inquisitor implements Good {
	private final String name = "Inquisitor";
	private final String description = "You're the inquisitor, your role is to investigate suspects at night and possibly find out who they are";
	
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
