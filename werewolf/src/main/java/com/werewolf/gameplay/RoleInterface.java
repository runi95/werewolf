package com.werewolf.gameplay;

import java.util.Random;

public interface RoleInterface {
	
	/** The name of the role **/
	public String getName();
	
	/** The alignment the role is fighting for **/
	public String getAlignment();
	
	/** A short descriptive requirement to win the game **/
	public String getGoal();
	
	/** Description of what the role does **/
	public String getDescription();

	public static RoleInterface getRandomRole() {
		Random random = new Random();
		Good[] town = Good.getAllGoodNonUnique();
		Evil[] evil = Evil.getAllEvilNonUnique();
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
