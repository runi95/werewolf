package com.werewolf.entities;

import javax.persistence.*;

@Entity
public class LobbyPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nickname", nullable = false, updatable = true)
    private String nickname;

    @ManyToOne
    LobbyEntity lobby;

    @OneToOne
    @JoinTable(name = "lobbyplayer_user", joinColumns = @JoinColumn(name = "lobbyplayer_nickname"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User user;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNickname() {
        return nickname;
    }

    public User getUser() {
        return user;
    }
}
