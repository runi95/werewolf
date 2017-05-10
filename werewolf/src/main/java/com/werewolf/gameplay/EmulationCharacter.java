package com.werewolf.gameplay;

import java.util.LinkedList;

/**
 * A dummy class for emulating game rounds
 */

public class EmulationCharacter {
	private String username, playerid, targetid;
	private RoleInterface role;
	private LinkedList<String> messageList = new LinkedList<>();
	
	public EmulationCharacter(String username, String playerid, RoleInterface role, String targetid) {
		this.username = username;
		this.playerid = playerid;
		this.role = role;
		this.targetid = targetid;
	}

	public String getUsername() {
		return username;
	}
	
	public String getPlayerid() {
		return playerid;
	}
	
	public RoleInterface getRole() {
		return role;
	}
	
	public String getTargetid() {
		return targetid;
	}
	
	public void setRole(RoleInterface role) {
		this.role = role;
	}
	
	public LinkedList<String> getMessageList() {
		return messageList;
	}
	
	public void addMessage(String message) {
		messageList.add(message);
	}
	
	public String getInquestMessage() {
		return role.getInquestMessage();
	}
	
}
