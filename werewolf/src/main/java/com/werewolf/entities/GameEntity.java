package com.werewolf.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class GameEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lobbyentityid", nullable = false, updatable = true)
    @Basic(optional = false)
    private long lobbyentityid;

    @Column(name = "gameid")
    private String gameid;
    
    @OneToMany(mappedBy = "lobby", fetch = FetchType.EAGER, targetEntity = LobbyPlayer.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "lobbyplayers")
    Set<GamePlayer> lobbyplayers;

    @Column(name = "rounds", nullable = false)
    private int rounds = 0;

    @ManyToMany
    @JoinTable(
            name = "gameentity_alive_ingameplayer",
            joinColumns = @JoinColumn(name = "gameid", referencedColumnName = "gameid"),
            inverseJoinColumns = @JoinColumn(name = "nickname", referencedColumnName = "nickname")
    )
    private List<GamePlayer> alivePlayers;

    @ManyToMany
    @JoinTable(
            name = "gameentity_dead_ingameplayer",
            joinColumns = @JoinColumn(name = "gameid", referencedColumnName = "gameid"),
            inverseJoinColumns = @JoinColumn(name = "nickname", referencedColumnName = "nickname")
    )
    private List<GamePlayer> deadPlayers;

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public void setAlivePlayers(List<GamePlayer> alivePlayers) {
        this.alivePlayers = alivePlayers;
    }

    public void setDeadPlayers(List<GamePlayer> deadPlayers) {
        this.deadPlayers = deadPlayers;
    }

    public void setGameid(String gameid) {
        this.gameid = gameid;
    }
    
    public String getGameId() {
        return gameid;
    }

    public int getRounds() {
        return rounds;
    }

    public List<GamePlayer> getAlivePlayers() {
        return alivePlayers;
    }

    public List<GamePlayer> getDeadPlayers() {
        return deadPlayers;
    }


}
