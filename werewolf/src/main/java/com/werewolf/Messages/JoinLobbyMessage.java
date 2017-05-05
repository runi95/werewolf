package com.werewolf.Messages;

public class JoinLobbyMessage {
    private String action;
    private String playerid;

    public String getAction() {
        return action;
    }
    
    public String getPlayerid() {
    	return playerid;
    }

    public void setAction(String action) {
        this.action = action;
    }
    
    public void setPlayerid(String playerid) {
    	this.playerid = playerid;
    }
}
