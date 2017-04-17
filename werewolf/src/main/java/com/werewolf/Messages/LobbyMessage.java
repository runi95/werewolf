package com.werewolf.Messages;

public class LobbyMessage {
    private String playerid;
    private String nickname;
    private String action;

    public LobbyMessage() {
    }

    public LobbyMessage(String playerid, String nickname, String action) {
        this.playerid = playerid;
        this.nickname = nickname;
        this.action = action;
    }

    public String getPlayerid() {
        return playerid;
    }

    public void setPlayerid(String playerid) {
        this.playerid = playerid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
