package com.werewolf.entities;

import javax.persistence.*;

@Entity
public class LobbyPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nickname", nullable = false, updatable = true)
    private String nickname;

    @OneToOne
    @JoinTable(name = "lobbyplayer_user", joinColumns = @JoinColumn(name = "lobbyplayer_nickname"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User user;

    public String getNickname() {
        return nickname;
    }

    public User getUser() {
        return user;
    }
}
