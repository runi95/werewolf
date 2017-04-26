package com.werewolf.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class GamePlayer {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = true)
    private long id;
	
	@Column(name = "nickname", nullable = false, updatable = true)
    private String nickname;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "game_gameplayers")
    GameEntity game;
	
	@OneToOne
    @JoinTable(name = "lobbyplayer_user", joinColumns = @JoinColumn(name = "lobbyplayer_nickname"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User user;
	
	@OneToOne
	@Column(name = "role", nullable = false, updatable = true)
	private String role;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public GameEntity getGame() {
		return game;
	}

	public void setGame(GameEntity game) {
		this.game = game;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}
