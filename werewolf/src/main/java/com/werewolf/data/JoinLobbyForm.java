package com.werewolf.data;

public class JoinLobbyForm {
    private String nickname;

    private String gameid;

    private long userid;

    public String getNickname() {
        return nickname;
    }

    public String getGameid() {
        return gameid;
    }

    public long getUserid() {
        return userid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public void setUserid(long userid) { this.userid = userid; }
}
