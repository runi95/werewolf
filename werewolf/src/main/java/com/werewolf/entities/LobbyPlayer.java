package com.werewolf.entities;

import com.werewolf.gameplay.RoleInterface;

public class LobbyPlayer {
    private String id;

    private String nickname;

    private LobbyEntity lobby;

    private User user;

    private String voted;
    
    private String target;
    
    private int votes;
    
    private boolean ready = false;
    
    private RoleInterface role;
    
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
    
    public void setTarget(String target) {
    	this.target = target;
    }
    
    public void setVotes(int votes) {
    	this.votes = votes;
    }
    
    public void setRole(RoleInterface role) {
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
    
    public String getTarget() {
    	return target;
    }
    
    public int getVotes() {
    	return votes;
    }
    
    public RoleInterface getRole() {
    	return role;
    }
    
    public String getAlignment() {
    	return alignment;
    }
}
