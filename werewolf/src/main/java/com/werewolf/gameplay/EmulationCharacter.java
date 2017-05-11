package com.werewolf.gameplay;

import java.util.LinkedList;

import com.werewolf.entities.LobbyPlayer;

/**
 * A dummy class for emulating game rounds
 */

public class EmulationCharacter {
	private LobbyPlayer lobbyPlayer;
	private String targetid;
	private RoleInterface role;
	private LinkedList<String> messageList = new LinkedList<>();
	
	public EmulationCharacter(LobbyPlayer lobbyPlayer, RoleInterface role, String targetid) {
		this.lobbyPlayer = lobbyPlayer;
		this.role = role;
		this.targetid = targetid;
	}

	public LobbyPlayer getLobbyPlayer() {
		return lobbyPlayer;
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
