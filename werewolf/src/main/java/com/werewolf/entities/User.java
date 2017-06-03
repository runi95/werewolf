package com.werewolf.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = true, unique = true)
    private long id;
	
	@Column(name = "username", nullable = false, unique = true)
    private String username;
	
	@Column(name = "passwordhash", nullable = false)
    private String passwordHash;
	
	@ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<UserRight> rights;

	@Column(name = "wins", nullable = false)
	private int wins = 0;

	@Column(name = "games", nullable = false)
	private int games = 0;

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getGames() {
		return games;
	}

	public void setGames(int games) {
		this.games = games;
	}

	public long getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPasswordHash() {
		return passwordHash;
	}
	
	public Set<UserRight> getRights() {
		return rights;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public void setRights(Set<UserRight> rights) {
		this.rights = rights;
	}
}
