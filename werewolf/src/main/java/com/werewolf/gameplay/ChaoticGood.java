package com.werewolf.gameplay;

import java.util.Random;

import com.werewolf.gameplay.roles.Amnesiac;

public interface ChaoticGood extends RoleInterface {
	public static final String alignment = "Chaotic Good";
	public static final String goal = "Survive and kill all evildoers";
	
	public static final ChaoticGood[] chaoticGoodUnique = new ChaoticGood[] {};
	public static final ChaoticGood[] chaoticGoodNonUnique = new ChaoticGood[] { new Amnesiac() };

	/**
	 * Unique roles are limited to only one player per game
	 * 
	 * @return list of unique chaotic good roles
	 */
	public static ChaoticGood[] getAllChaoticGoodUnique() {
		return chaoticGoodUnique;
	}

	public static ChaoticGood[] getAllChaoticGoodNonUnique() {
		return chaoticGoodNonUnique;
	}

	public static ChaoticGood[] getAllChaoticGood() {
		ChaoticGood[] unique = getAllChaoticGoodUnique();
		ChaoticGood[] nonUnique = getAllChaoticGoodNonUnique();
		ChaoticGood[] all = new ChaoticGood[unique.length + nonUnique.length];
		for (int i = 0; i < unique.length; i++)
			all[i] = unique[i];

		for (int i = 0; i < nonUnique.length; i++)
			all[unique.length + i] = nonUnique[i];

		return all;
	}

	public static ChaoticGood getRandomChaoticGood() {
		Random random = new Random();
		int rng = random.nextInt(getAllChaoticGoodNonUnique().length);
		ChaoticGood randomChaoticGood = getAllChaoticGoodNonUnique()[rng];

		return randomChaoticGood;
	}
}
