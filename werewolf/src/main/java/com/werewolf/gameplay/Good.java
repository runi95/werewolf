package com.werewolf.gameplay;

import java.util.Random;

import com.werewolf.gameplay.roles.Bard;
import com.werewolf.gameplay.roles.Guard;
import com.werewolf.gameplay.roles.Inquisitor;
import com.werewolf.gameplay.roles.King;
import com.werewolf.gameplay.roles.Priest;

public interface Good extends RoleInterface {
	public static final String alignment = "Good";
	public static final String goal = "Survive and kill all evildoers";
	
	public static final Good[] goodUnique = new Good[] {new King()};
	public static final Good[] goodNonUnique = new Good[] {new Inquisitor(), new Bard(), new Priest(), new Guard()};

	/**
	 * Unique roles are limited to only one player per game
	 * 
	 * @return list of unique good roles
	 */
	public static Good[] getAllGoodUnique() {
		return goodUnique;
	}
	
	public static Good[] getAllGoodNonUnique() {
		return goodNonUnique;
	}

	public static Good[] getAllGood() {
		Good[] unique = getAllGoodUnique();
		Good[] nonUnique = getAllGoodNonUnique();
		Good[] all = new Good[unique.length + nonUnique.length];
		for(int i = 0; i < unique.length; i++)
			all[i] = unique[i];
		
		for(int i = 0; i < nonUnique.length; i++)
			all[unique.length + i] = nonUnique[i];
		
		return all;
	}

	public static Good getRandomGood() {
		Random random = new Random();
		int rng = random.nextInt(getAllGoodNonUnique().length);
		Good randomGood = getAllGoodNonUnique()[rng];
		
		return randomGood;
	}
}
