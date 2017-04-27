package com.werewolf.gameplay;

import java.util.Random;

import com.werewolf.gameplay.roles.Bandit;
import com.werewolf.gameplay.roles.Marauder;

public interface Evil extends RoleInterface {
	public static final String alignment = "Evil";
	public static final String goal = "Survive and kill every member of the town";
	
	public static final Evil[] evilUnique = new Evil[] {new Marauder()};
	public static final Evil[] evilNonUnique = new Evil[] {new Bandit()};

	/**
	 * Unique roles are limited to only one player per game
	 * 
	 * @return list of unique evil roles
	 */
	public static Evil[] getAllEvilUnique() {
		return evilUnique;
	}
	
	public static Evil[] getAllEvilNonUnique() {
		return evilNonUnique;
	}

	public static Evil[] getAllEvil() {
		Evil[] unique = getAllEvilUnique();
		Evil[] nonUnique = getAllEvilNonUnique();
		Evil[] all = new Evil[unique.length + nonUnique.length];
		for(int i = 0; i < unique.length; i++)
			all[i] = unique[i];
		
		for(int i = 0; i < nonUnique.length; i++)
			all[unique.length + i] = nonUnique[i];
		
		return all;
	}

	public static Evil getRandomEvil() {
		Random random = new Random();
		int rng = random.nextInt(getAllEvilNonUnique().length);
		Evil randomEvil = getAllEvilNonUnique()[rng];
		
		return randomEvil;
	}
}
