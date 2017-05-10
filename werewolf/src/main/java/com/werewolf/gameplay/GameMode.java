package com.werewolf.gameplay;

import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;

public interface GameMode {
	
	/** This is the first thing that happens when the lobby turns into a game **/
	public void initalizeGame(LobbyEntity lobbyEntity);
	
	public void vote(LobbyEntity lobbyEntity, LobbyPlayer voter, LobbyPlayer voteTarget, LobbyPlayer oldVoteTarget, boolean status);
	
	public void nightAction(LobbyEntity lobbyEntity, LobbyPlayer acter, LobbyPlayer oldTarget, LobbyPlayer target, boolean act);

}
