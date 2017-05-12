package com.werewolf.gameplay.roles;

import com.werewolf.gameplay.EmulationCharacter;
import com.werewolf.gameplay.Evil;
import com.werewolf.gameplay.GameEmulator;

public class Marauder implements Evil {
	private final String name = "Maurader";
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
	
	@Override
	public String getInquestMessage() {
		return "Your target seems to be of high importance. (King, Maurader)";
	}

	@Override
	public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
		game.forceEvilKill(self, target);
	}
}
