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

    private RoleInterface roleMask = null;
    
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

    public void setRoleMask(RoleInterface roleMask) { this.roleMask = roleMask; }
    
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
    	if(roleMask != null)
    	    return roleMask;
    	else
    	    return role;
    }

    public RoleInterface getRealRole() { return role; }

    public RoleInterface getRoleMask() { return roleMask; }
    
    public String getAlignment() {
    	return alignment;
    }
}
