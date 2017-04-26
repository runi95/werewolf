package com.werewolf.gameplay;

import java.util.Random;

public interface ChaoticEvil extends RoleInterface {
	public static final String goal = "Survive and until every member of the town is dead";
	
	public static final ChaoticEvil[] chaoticEvilUnique = new ChaoticEvil[] {};
	public static final ChaoticEvil[] chaoticEvilNonUnique = new ChaoticEvil[] {};

	/**
	 * Unique roles are limited to only one player per game
	 * 
	 * @return list of unique chaotic evil roles
	 */
	public static ChaoticEvil[] getAllChaoticEvilUnique() {
		return chaoticEvilUnique;
	}

	public static ChaoticEvil[] getAllChaoticEvilNonUnique() {
		return chaoticEvilNonUnique;
	}

	public static ChaoticEvil[] getAllChaoticEvil() {
		ChaoticEvil[] unique = getAllChaoticEvilUnique();
		ChaoticEvil[] nonUnique = getAllChaoticEvilNonUnique();
		ChaoticEvil[] all = new ChaoticEvil[unique.length + nonUnique.length];
		for (int i = 0; i < unique.length; i++)
			all[i] = unique[i];

		for (int i = 0; i < nonUnique.length; i++)
			all[unique.length + i] = nonUnique[i];

		return all;
	}

	public static ChaoticEvil getRandomChaoticEvil() {
		Random random = new Random();
		int rng = random.nextInt(getAllChaoticEvilNonUnique().length);
		ChaoticEvil randomChaoticEvil = getAllChaoticEvilNonUnique()[rng];

		return randomChaoticEvil;
	}
}
