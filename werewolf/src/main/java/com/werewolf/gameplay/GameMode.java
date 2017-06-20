package com.werewolf.gameplay;

import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;

/**
 * This is the interface for any GameMode, every public method
 * is going to be used by the JoinLobbyServiceImpl
 * <p>
 * Required variable for every GameMode class:
 *  "@Autowired
 *  SimpMessagingTemplate simpTemplate;"
 * <p>
 * broadcastMessage() and / or privateMessage() messages are messages sent to clients,
 * information about the different messages should be contained in
 * the method's javadoc.
 */
public interface GameMode {
	
	/** This is the first thing that happens when the lobby turns into a game 
	 * 	should run lobbyEntity.setStartedState(true);
	 * 	@param lobbyEntity
	 */
	public void initalizeGame(LobbyEntity lobbyEntity);
	
	/** This method is responsible for setting a role and alignment for every player in the lobby
	 */
	public void setRoles(LobbyEntity lobbyEntity);
	
	/** 
	 * Runs whenever someone votes, this method should update voteTarget's vote counter
	 * and check if there are enough voters to lynch a player.
	 * 
	 * to add a vote:
	 * broadcastMessage("updatevotestatus", voterid, voteTargetid, voteCounter,"+");
	 * to remove a vote:
	 * broadcastMessage("updatevotestatus", voterid, voteTargetid, voteCounter,"-");
	 * @param lobbyEntity
	 * @param voter the player voting
	 * @param voteTarget the player voted on
	 * @param oldVoteTarget the old player voted on (may be null)
	 * @param status true if player put his vote and false if player undid his vote
	 */
	public void vote(LobbyEntity lobbyEntity, LobbyPlayer voter, LobbyPlayer voteTarget, LobbyPlayer oldVoteTarget, boolean status);
	
	/**
	 * Runs whenever a player has set his target at night, this method should update
	 * the acter's target.
	 * 
	 * to select a target:
	 * privateMessage("nightaction", targetid);
	 * to unselect a target:
	 * privateMessage("unnightaction", targetid);
	 * @param lobbyEntity
	 * @param acter the player performing his action
	 * @param oldTarget the old target of acter's action (may be null)
	 * @param target the current target of acter's action
	 * @param act true if target was selected and false if target was unselected
	 */
	public void nightAction(LobbyEntity lobbyEntity, LobbyPlayer acter, LobbyPlayer oldTarget, LobbyPlayer target, boolean act);

	/**
	 * This is the name that is sent to the clients when requesting the game mode.
	 * @return name
	 */
	public String getName();
}
