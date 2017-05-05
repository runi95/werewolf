package com.werewolf.entities;

import javax.persistence.*;

@Entity
public class LobbyPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = true)
    private String id;

    @Column(name = "nickname", nullable = false, updatable = true)
    private String nickname;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "lobby_lobbyplayers")
    LobbyEntity lobby;

    @OneToOne
    private User user;

    private String voted;
    
    private int votes;
    
    @Column(name = "ready")
    private boolean ready = false;
    
    private String role;
    
    private String alignment;
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setLobby(LobbyEntity lobby) { this.lobby = lobby; }

    public void setUser(User user) {
        this.user = user;
    }

    public void setReady(boolean ready) {
    	this.ready = ready;
    }
    
    public void setVoted(String voted) {
    	this.voted = voted;
    }
    
    public void setVotes(int votes) {
    	this.votes = votes;
    }
    
    public void setRole(String role) {
    	this.role = role;
    }
    
    public void setAlignment(String alignment) {
    	this.alignment = alignment;
    }
    
    public String getId() { return id; }

    public String getNickname() {
        return nickname;
    }

    public LobbyEntity getLobby() { return lobby; }

    public User getUser() {
        return user;
    }

    public boolean ready() {
    	return ready;
    }
    
    public String getVoted() {
    	return voted;
    }
    
    public int getVotes() {
    	return votes;
    }
    
    public String getRole() {
    	return role;
    }
    
    public String getAlignment() {
    	return alignment;
    }
    
	@Override
	public String toString() {
		return "LobbyPlayer [id=" + id + ", nickname=" + nickname + ", lobby=" + lobby + ", user=" + user + "]";
	}
    
}
