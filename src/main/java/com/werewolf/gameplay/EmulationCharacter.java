package com.werewolf.gameplay;

import com.werewolf.Messages.LobbyMessage;
import com.werewolf.entities.LobbyPlayer;

import java.util.LinkedList;

/**
 * A dummy class for emulating game rounds
 */

public class EmulationCharacter {
	private final LobbyPlayer lobbyPlayer;
	private String targetid;
	private RoleInterface role;
	private LinkedList<LobbyMessage> messageList = new LinkedList<>();
	
	public EmulationCharacter(final LobbyPlayer lobbyPlayer, RoleInterface role, String targetid) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lobbyPlayer.getId() == null) ? 0 : lobbyPlayer.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmulationCharacter other = (EmulationCharacter) obj;
		if (lobbyPlayer.getId() == null) {
			if (other.lobbyPlayer.getId() != null)
				return false;
		} else if (!lobbyPlayer.getId().equals(other.lobbyPlayer.getId()))
			return false;
		return true;
	}
	
	
}
