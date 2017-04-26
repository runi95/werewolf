package com.werewolf.gameplay;

import java.util.Random;

public interface NeutralEvil extends RoleInterface {
	public static final String alignment = "Neutral Evil";
	public static final String goal = "Survive until all members of town are dead";
	
	public static final NeutralEvil[] NeutralEvilUnique = new NeutralEvil[] {};
	public static final NeutralEvil[] NeutralEvilNonUnique = new NeutralEvil[] {};

	/**
	 * Unique roles are limited to only one player per game
	 * 
	 * @return list of unique neutral evil roles
	 */
	public static NeutralEvil[] getAllNeutralEvilUnique() {
		return NeutralEvilUnique;
	}
	
	public static NeutralEvil[] getAllNeutralEvilNonUnique() {
		return NeutralEvilNonUnique;
	}

	public static NeutralEvil[] getAllNeutralEvil() {
		NeutralEvil[] unique = getAllNeutralEvilUnique();
		NeutralEvil[] nonUnique = getAllNeutralEvilNonUnique();
		NeutralEvil[] all = new NeutralEvil[unique.length + nonUnique.length];
		for(int i = 0; i < unique.length; i++)
			all[i] = unique[i];
		
		for(int i = 0; i < nonUnique.length; i++)
			all[unique.length + i] = nonUnique[i];
		
		return all;
	}

	public static NeutralEvil getRandomNeutralEvil() {
		Random random = new Random();
		int rng = random.nextInt(getAllNeutralEvilNonUnique().length);
		NeutralEvil randomNeutralEvil = getAllNeutralEvilNonUnique()[rng];
		
		return randomNeutralEvil;
	}
}
