package com.werewolf.gameplay;

import java.util.LinkedList;

import com.werewolf.Messages.LobbyMessage;
import com.werewolf.entities.LobbyPlayer;

/**
 * A dummy class for emulating game rounds
 */

public class EmulationCharacter {
	private LobbyPlayer lobbyPlayer;
	private String targetid;
	private RoleInterface role;
	private LinkedList<LobbyMessage> messageList = new LinkedList<>();
	
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
	
	public LinkedList<LobbyMessage> getMessageList() {
		return messageList;
	}
	
	public void addNightMessage(String message) {
		addMessage(new LobbyMessage("nightmessage", message));
	}
	
	public void addMessage(LobbyMessage message) {
		messageList.add(message);
	}
	
	public String getInquestMessage() {
		return role.getInquestMessage();
	}
	
}
