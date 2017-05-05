package com.werewolf.gameplay;

import java.util.LinkedList;

/**
 * A dummy class for emulating game rounds
 */

public class EmulationCharacter {
	private String playerid;
	private RoleInterface role;
	private LinkedList<String> messageList = new LinkedList<>();
	
	public EmulationCharacter(String playerid, RoleInterface role) {
		this.playerid = playerid;
		this.role = role;
	}

	public String getPlayerid() {
		return playerid;
	}
	
	public RoleInterface getRole() {
		return role;
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
