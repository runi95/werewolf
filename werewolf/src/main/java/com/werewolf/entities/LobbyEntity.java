package com.werewolf.entities;

import javax.persistence.*;
import java.util.*;

@Entity
public class LobbyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lobbyentityid", nullable = false, updatable = true)
    @Basic(optional = false)
    private long lobbyentityid;

    @Column(name = "gameid")
    private String gameid;
    
    @Column(name = "rounds", nullable = false)
    private int rounds = 0;

    @OneToMany(mappedBy = "lobby", fetch = FetchType.EAGER, targetEntity = LobbyPlayer.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "lobbyplayers")
    Set<LobbyPlayer> lobbyplayers;
    
    @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL)
    private Set<LobbyPlayer> alivePlayers;

    @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL)
    private Set<LobbyPlayer> deadPlayers;
    
    @Column(name = "readyplayercount")
    private int readyPlayerCount = 0;
    
    @Column(name = "gamestarted")
    private boolean gamestarted = false;
    
    public boolean getStartedState() {
    	return gamestarted;
    }
    
    public int getRounds() {
		return rounds;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public Set<LobbyPlayer> getAlivePlayers() {
		return alivePlayers;
	}

	public void setAlivePlayers(Set<LobbyPlayer> alivePlayers) {
		this.alivePlayers = alivePlayers;
	}

	public Set<LobbyPlayer> getDeadPlayers() {
		return deadPlayers;
	}

	public void setDeadPlayers(Set<LobbyPlayer> deadPlayers) {
		this.deadPlayers = deadPlayers;
	}

	public void setStartedState(boolean gamestarted) {
    	this.gamestarted = gamestarted;
    }

    public long getlobbyentityid() {
        return lobbyentityid;
    }

    public String getGameId() {
        return gameid;
    }

    public Set<LobbyPlayer> getPlayers() {
        return lobbyplayers;
    }

    public int getReadyPlayerCount() {
    	return readyPlayerCount;
    }
    
    public void setGameid(String gameid) {
        this.gameid = gameid;
    }

    public void removePlayer(LobbyPlayer player) {
        if(!lobbyplayers.contains(player))
            return;

        lobbyplayers.remove(player);
    }

    public void setPlayers(Set<LobbyPlayer> lobbyplayers) {
        this.lobbyplayers = lobbyplayers;
    }
    
    public void addPlayer(LobbyPlayer lobbyPlayer) {
    	if(lobbyplayers == null)
    		lobbyplayers = new HashSet<LobbyPlayer>();
    	
    	lobbyPlayer.setLobby(this);
    	lobbyplayers.add(lobbyPlayer);
    }

    public void setlobbyentityid(long lobbyentityid) {
        this.lobbyentityid = lobbyentityid;
    }

    public void setReadyPlayerCount(int readyPlayerCount) {
    	this.readyPlayerCount = readyPlayerCount;
    }
    
	@Override
	public String toString() {
		return "LobbyEntity [lobbyentityid=" + lobbyentityid + ", gameid=" + gameid	+ "]";
	}
    
}
