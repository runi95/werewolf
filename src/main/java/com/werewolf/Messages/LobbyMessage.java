package com.werewolf.Messages;

public class LobbyMessage {
	private String action;
    private String playerid;
    private String info;
    private String additionalinfo;
    private String variable;

    public LobbyMessage() {
    }

    public LobbyMessage(String action) {
    	this.action = action;
    }
    
    public LobbyMessage(String action, String info) {
    	this.action = action;
    	this.info = info;
    }
    
    public LobbyMessage(String action, String playerid, String info) {
    	this.action = action;
    	this.playerid = playerid;
        this.info = info;
    }
    
    public LobbyMessage(String action, String playerid, String info, String additionalinfo) {
		this.action = action;
		this.playerid = playerid;
		this.info = info;
		this.additionalinfo = additionalinfo;
	}
    
    public LobbyMessage(String action, String playerid, String info, String additionalinfo, String variable) {
		this.action = action;
		this.playerid = playerid;
		this.info = info;
		this.additionalinfo = additionalinfo;
		this.variable = variable;
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}


	public String getAdditionalinfo() {
		return additionalinfo;
	}

	public void setAdditionalinfo(String additionalinfo) {
		this.additionalinfo = additionalinfo;
	}
	
	public String getVariable() {
		return variable;
	}
	
	public void setVariable(String variable) {
		this.variable = variable;
	}
}
