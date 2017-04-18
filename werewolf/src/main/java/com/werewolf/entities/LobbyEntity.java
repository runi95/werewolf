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

    @OneToMany(mappedBy = "lobby", fetch = FetchType.EAGER, targetEntity = LobbyPlayer.class, cascade = CascadeType.ALL)
    @Column(name = "lobbyplayers")
    Set<LobbyPlayer> lobbyplayers;

    public long getlobbyentityid() {
        return lobbyentityid;
    }

    public String getGameId() {
        return gameid;
    }

    public Set<LobbyPlayer> getPlayers() {
        return lobbyplayers;
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

	@Override
	public String toString() {
		return "LobbyEntity [lobbyentityid=" + lobbyentityid + ", gameid=" + gameid	+ "]";
	}
    
}
