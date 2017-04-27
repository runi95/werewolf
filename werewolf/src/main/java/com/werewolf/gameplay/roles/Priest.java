package com.werewolf.gameplay.roles;

import com.werewolf.gameplay.EmulationCharacter;
import com.werewolf.gameplay.GameEmulator;
import com.werewolf.gameplay.Good;

public class Priest implements Good {
	public final String name = "Priest";
	public final String description = "You're the town priest, your role is to make sure everyone makes it through the night, you get to heal someone every night and make sure they can't die";
	
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
		return "Your target seems to be popular amongst others. (Bard, Priest, Illusionist)";
	}

	@Override
	public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
		game.heal(self, target);
	}

}