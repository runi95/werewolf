package com.werewolf.gameplay.roles;

import com.werewolf.gameplay.Good;

public class Guard implements Good {
	private final String name = "Guard";
	private final String description = "You are the town Guard, you're here to serve and protect, you'll be awake all night protecting the target of your choice, be careful though as you could accidentally protect an evildoer!";
	
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
