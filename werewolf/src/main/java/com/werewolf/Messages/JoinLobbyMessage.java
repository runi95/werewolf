package com.werewolf.Messages;

public class JoinLobbyMessage {

	private String action, playerid;
	
	public void setPlayerid(String playerid) {
		this.playerid = playerid;
	}
	
	public String getPlayerid() {
		return playerid;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getAction() {
		return action;
	}
}
