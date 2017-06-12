package com.werewolf.gameplay;

import java.util.Random;

public interface RoleInterface {
	/** The name of the role **/
	public String getName();

	/** The alignment of the character **/
	public Alignments getAlignment();

	/** Description of what the role does **/
	public String getDescription();

	public String getInquestMessage();

	public void doAction(GameEmulator game, EmulationCharacter self, EmulationCharacter target);

	public static RoleInterface getRandomRole() {
		Random random = new Random();
		Roles[] town = Alignments.Good.getAllRolesFromThisAlignment();
		Roles[] evil = Alignments.Evil.getAllRolesFromThisAlignment();
		int length = town.length + evil.length;
		RoleInterface[] allNonUnique = new RoleInterface[length];
		for(int i = 0; i < town.length; i++)
			allNonUnique[i] = town[i];
		
		for(int i = 0; i < evil.length; i++)
			allNonUnique[town.length + i] = evil[i];
		
		int rng = random.nextInt(length);
		RoleInterface randomRole = allNonUnique[rng];
		
		return randomRole;
	}
}
