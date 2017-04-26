package com.werewolf.gameplay.roles;

import com.werewolf.gameplay.Evil;

public class Boss implements Evil {
	private final String name = "Boss";
	private final String description = "You're the boss, the leader of an evil organisation that plans to take over the town, order someone to be killed or head out to kill them yourself if all your minions are dead";
	
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
