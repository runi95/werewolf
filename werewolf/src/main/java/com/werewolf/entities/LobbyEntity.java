package com.werewolf.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class LobbyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gameid", nullable = false, updatable = true)
    private String gameid;

    @ManyToMany
    @JoinTable(
            name = "gameentity_alive_ingameplayer",
            joinColumns = @JoinColumn(name = "gameid", referencedColumnName = "gameid"),
            inverseJoinColumns = @JoinColumn(name = "nickname", referencedColumnName = "nickname")
    )
    List<LobbyPlayer> players;

    public String getGameId() {
        return gameid;
    }

    public List<LobbyPlayer> getPlayers() {
        return players;
    }
}
