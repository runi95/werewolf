package com.werewolf.Messages;

public class ReadyAndUnreadyLobbyMessage {
	private String action;
	private String playerid;
	private String readyplayercount;
	private String lobbyplayercount;
	
	public ReadyAndUnreadyLobbyMessage(String playerid, String readyplayercount, String lobbyplayercount) {
		action = "updatereadystatus";
		this.playerid = playerid;
		this.readyplayercount = readyplayercount;
		this.lobbyplayercount = lobbyplayercount;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getPlayerid() {
		return playerid;
	}
	public void setPlayerid(String playerid) {
		this.playerid = playerid;
	}
	public String getReadyplayercount() {
		return readyplayercount;
	}
	public void setReadyplayercount(String readyplayercount) {
		this.readyplayercount = readyplayercount;
	}
	public String getLobbyplayercount() {
		return lobbyplayercount;
	}
	public void setLobbyplayercount(String lobbyplayercount) {
		this.lobbyplayercount = lobbyplayercount;
	}

}
