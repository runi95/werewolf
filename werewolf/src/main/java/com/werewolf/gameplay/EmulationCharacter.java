package com.werewolf.gameplay;

import java.util.LinkedList;

/**
 * A dummy class for emulating game rounds
 */

public class EmulationCharacter {
	private String name;
	private RoleInterface role;
	private LinkedList<String> messageList = new LinkedList<>();
	
	public EmulationCharacter(String name, RoleInterface role) {
		this.name = name;
		this.role = role;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
