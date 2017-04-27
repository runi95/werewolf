package com.werewolf.gameplay.roles;

import com.werewolf.gameplay.EmulationCharacter;
import com.werewolf.gameplay.GameEmulator;
import com.werewolf.gameplay.Good;

public class Bard implements Good {
	private final String name = "Bard";
	private final String description = "You're a bard, you're an expert at keeping others busy at night by occupying them, negating any and all of their actions that night";
	
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
		game.block(self, target);
	}

}
