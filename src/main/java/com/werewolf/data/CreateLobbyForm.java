package com.werewolf.data;

public class CreateLobbyForm {
    private String gamemode;

    private String privatelobby;

    private String maxplayers;

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGamemode() {
        return gamemode;
    }

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    public String getPrivatelobby() {
        return privatelobby;
    }

    public void setPrivatelobby(String privatelobby) {
        this.privatelobby = privatelobby;
    }

    public String getMaxplayers() {
        return maxplayers;
    }

    public void setMaxplayers(String maxplayers) {
        this.maxplayers = maxplayers;
    }
}
