package com.werewolf.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class LobbyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id", nullable = false, updatable = true)
    private String game_id;

    @ManyToMany
    @JoinTable(
            name = "gameentity_alive_ingameplayer",
            joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "nickname", referencedColumnName = "nickname")
    )
    List<LobbyPlayer> players;

    public String getGame_id() {
        return game_id;
    }

    public List<LobbyPlayer> getPlayers() {
        return players;
    }
}
