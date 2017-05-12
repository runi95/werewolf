package com.werewolf.gameplay;

import java.util.Random;

import com.werewolf.gameplay.roles.Jester;

public interface Neutral extends RoleInterface {
	public static final String alignment = "Neutral";
	public static final String goal = "Survive until the end of the game";
	
	public static final Neutral[] neutralUnique = new Neutral[] {};
	public static final Neutral[] neutralNonUnique = new Neutral[] { new Jester() };

	/**
	 * Unique roles are limited to only one player per game
	 * 
	 * @return list of unique neutral roles
	 */
	public static Neutral[] getAllNeutralUnique() {
		return neutralUnique;
	}
	
	public static Neutral[] getAllNeutralNonUnique() {
		return neutralNonUnique;
	}

	public static Neutral[] getAllNeutral() {
		Neutral[] unique = getAllNeutralUnique();
		Neutral[] nonUnique = getAllNeutralNonUnique();
		Neutral[] all = new Neutral[unique.length + nonUnique.length];
		for(int i = 0; i < unique.length; i++)
			all[i] = unique[i];
		
		for(int i = 0; i < nonUnique.length; i++)
			all[unique.length + i] = nonUnique[i];
		
		return all;
	}

	public static Neutral getRandomNeutral() {
		Random random = new Random();
		int rng = random.nextInt(getAllNeutralNonUnique().length);
		Neutral randomNeutral = getAllNeutralNonUnique()[rng];
		
		return randomNeutral;
	}
}
