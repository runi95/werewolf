package com.werewolf.gameplay.roles;

import com.werewolf.gameplay.EmulationCharacter;
import com.werewolf.gameplay.GameEmulator;
import com.werewolf.gameplay.Good;

public class King implements Good {
	private final String name = "King";
	private final String description = "You are the town's king, you can sway any vote in your favour, making you an incredibly important asset late in the game, beware though if you ever vote during the day you'll automatically reveal yourself as the king informing any and every evildoer out there of your power";
	
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
		// King does nothing at night
	}

}
