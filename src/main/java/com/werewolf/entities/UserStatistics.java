package com.werewolf.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class UserStatistics {

    //The name of the right is unique and therefore we use it as the primary key
    @Id
    @Column(name = "username", nullable = false, updatable = false, unique = true)
    private String username;

    @Column(name = "gamesplayed", nullable = false)
    private long gamesplayed;

    @Column(name = "gameswon", nullable = false)
    private long gameswon;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getGamesplayed() {
        return gamesplayed;
    }

    public void setGamesplayed(long gamesplayed) {
        this.gamesplayed = gamesplayed;
    }

    public long getGameswon() {
        return gameswon;
    }

    public void setGameswon(long gameswon) {
        this.gameswon = gameswon;
    }
}