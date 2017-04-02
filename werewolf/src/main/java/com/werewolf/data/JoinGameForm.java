package com.werewolf.data;

import org.hibernate.validator.constraints.NotEmpty;

public class JoinGameForm {

	@NotEmpty
	private String nickname;
	
	private String gameId;
	
	public String getNickname() {
		return nickname;
	}
	
	public String getGameId() {
		return gameId;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}
