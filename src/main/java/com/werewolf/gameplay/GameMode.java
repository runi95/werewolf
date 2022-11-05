package com.werewolf.gameplay;

import com.werewolf.entities.GamePhase;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;

import java.util.List;

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

    /**
     * A simple listener for GamePhase
     * @param oldGamePhase
     * @param newGamePhase
     */
    void gamePhaseChanges(GamePhase oldGamePhase, GamePhase newGamePhase);

	/** This is the first thing that happens when the lobby turns into a game 
	 * 	should run lobbyEntity.setStartedState(true);
	 * 	@param lobbyEntity
	 */
	void initalizeGame(LobbyEntity lobbyEntity);
	
	/** This method is responsible for setting a role and alignment for every player in the lobby
	 */
	List<RoleInterface> setRoles(LobbyEntity lobbyEntity);
	
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
	 * @param oldTarget the old player voted on (may be null)
	 * @param flag true if player put his vote and false if player undid his vote
	 */
	void vote(LobbyEntity lobbyEntity, LobbyPlayer voter, LobbyPlayer voteTarget, LobbyPlayer oldTarget, boolean flag);
	
	/**
	 * Runs whenever a player has set his target at night, this method should update
	 * the acter's target.
	 * 
	 * to select a target:
	 * privateMessage("nightaction", targetid);
	 * to unselect a target:
	 * privateMessage("unnightaction", targetid);
	 * @param lobbyEntity
	 * @param actor the player performing his action
	 * @param target the current target of acter's action
     * @param oldTarget the old target of acter's action (may be null)
	 * @param flag true if target was selected and false if target was unselected
	 */
    void nightAction(LobbyEntity lobbyEntity, LobbyPlayer actor, LobbyPlayer target, LobbyPlayer oldTarget, boolean flag);

	/**
	 * This is the name that is sent to the clients when requesting the game mode.
	 * @return name
	 */
	String getName();
}
