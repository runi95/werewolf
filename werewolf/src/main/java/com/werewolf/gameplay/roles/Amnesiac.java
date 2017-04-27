package com.werewolf.gameplay.roles;

import com.werewolf.gameplay.ChaoticGood;
import com.werewolf.gameplay.EmulationCharacter;
import com.werewolf.gameplay.GameEmulator;

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

	@Override
	public String getInquestMessage() {
		return "Your target seems to be a lunatic. (Amnesiac, Jester)";
	}
	
	@Override
	public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target) {
		game.amnesiac(self);
	}

}
