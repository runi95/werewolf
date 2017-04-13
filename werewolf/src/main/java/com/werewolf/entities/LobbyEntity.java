package com.werewolf.entities;

import javax.persistence.*;
import java.util.*;

@Entity
public class LobbyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = true)
    private long id;

    @Column(name = "gameid")
    private String gameid;

    @OneToMany(mappedBy = "lobby")
    List<LobbyPlayer> lobbyplayers = new ArrayList<>();

    public long getId() {
        return id;
    }

    public String getGameId() {
        return gameid;
    }

    public List<LobbyPlayer> getPlayers() {
        return lobbyplayers;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public void addPlayer(LobbyPlayer player) {
        if(lobbyplayers.contains(player))
            return;

        lobbyplayers.add(player);
    }

    public void removePlayer(LobbyPlayer player) {
        if(!lobbyplayers.contains(player))
            return;

        lobbyplayers.remove(player);
    }

    public void setId(long id) {
        this.id = id;
    }
}
