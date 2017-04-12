package com.werewolf.data;

import com.werewolf.entities.User;

public class JoinLobbyForm {
    private String nickname;

    private String gameid;

    private long userid;

    private User user;

    public String getNickname() {
        return nickname;
    }

    public String getGameid() {
        return gameid;
    }

    public long getUserid() {
        return userid;
    }

    public User getUser() { return user; }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public void setUserid(long userid) { this.userid = userid; }

    public void setUser(User user) { this.user = user; }
}
