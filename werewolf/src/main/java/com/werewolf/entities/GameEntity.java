package com.werewolf.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gameid", nullable = false, updatable = true)
    private String gameid;

    @Column(name = "rounds", nullable = false)
    private int rounds;

    @ManyToMany
    @JoinTable(
            name = "gameentity_alive_ingameplayer",
            joinColumns = @JoinColumn(name = "gameid", referencedColumnName = "gameid"),
            inverseJoinColumns = @JoinColumn(name = "nickname", referencedColumnName = "nickname")
    )
    private List<InGamePlayer> alivePlayers;

    @ManyToMany
    @JoinTable(
            name = "gameentity_dead_ingameplayer",
            joinColumns = @JoinColumn(name = "gameid", referencedColumnName = "gameid"),
            inverseJoinColumns = @JoinColumn(name = "nickname", referencedColumnName = "nickname")
    )
    private List<InGamePlayer> deadPlayers;

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public void setAlivePlayers(List<InGamePlayer> alivePlayers) {
        this.alivePlayers = alivePlayers;
    }

    public void setDeadPlayers(List<InGamePlayer> deadPlayers) {
        this.deadPlayers = deadPlayers;
    }

    public String getGame_id() {
        return gameid;
    }

    public int getRounds() {
        return rounds;
    }

    public List<InGamePlayer> getAlivePlayers() {
        return alivePlayers;
    }

    public List<InGamePlayer> getDeadPlayers() {
        return deadPlayers;
    }


}
