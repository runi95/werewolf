package com.werewolf.entities;

public class LobbyPlayer {
    private String id;

    private String nickname;

    private LobbyEntity lobby;

    private User user;

    private String voted;
    
    private int votes;
    
    private boolean ready = false;
    
    private String role;
    
    private String alignment;
    
    public LobbyPlayer(String id, User user, LobbyEntity lobby) {
    	this.id = id;
    	this.user = user;
    	this.lobby = lobby;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
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
}
