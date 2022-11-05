package com.werewolf.entities;

import com.werewolf.gameplay.Game;
import com.werewolf.gameplay.RuleModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LobbyEntity {

	// ID used to join this lobby with
	private String gameid;

	// How many rounds have this lobby been through?
	private int rounds = 0;

	private GamePhase gamePhase = GamePhase.LOBBY;

	// How much time is left on this phase?
	private int phaseTime = 0;

	private final int maxPlayers, minPlayers;

	private boolean privateLobby = false;

	private Map<String, LobbyPlayer> lobbyplayers = new HashMap<String, LobbyPlayer>();
	private Map<String, LobbyPlayer> alivePlayers = new HashMap<String, LobbyPlayer>();
	private Map<String, LobbyPlayer> deadPlayers = new HashMap<String, LobbyPlayer>();
	private Map<String, LobbyPlayer> teamEvil = new HashMap<String, LobbyPlayer>();

	// Amount of players that has clicked ready during the lobby phase
	private int readyPlayerCount = 0;

	// True if the lobby is in-game and false otherwise
	private boolean gamestarted = false;
	
	private RuleModel ruleModel;

	private final Game game;

	public LobbyEntity(String gameid, int maxPlayers, Game game, RuleModel ruleModel) {
		this.gameid = gameid;
		this.game = game;
		this.ruleModel = ruleModel;
		this.maxPlayers = Math.max(this.ruleModel.getMinPlayerAmount(), Math.min(this.ruleModel.getMaxPlayerAmount(), maxPlayers));
		this.minPlayers = ruleModel.getMinPlayerAmount();
	}

	public boolean getPrivate() { return privateLobby; }

	public void setPrivate(boolean privateLobby) { this.privateLobby = privateLobby; }

    public RuleModel getRuleModel() {
        return ruleModel;
    }

    public void setRuleModel(RuleModel ruleModel) {
	    this.ruleModel = ruleModel;
    }

    public boolean getStartedState() {
		return gamestarted;
	}

	public int getRounds() {
		return rounds;
	}

	public void setRounds(int rounds) {
		this.rounds = rounds;
	}

	public GamePhase getPhase() {
		return gamePhase;
	}

	public synchronized void setPhase(GamePhase gamePhase) {
		game.gamePhaseChanges(this.gamePhase, gamePhase, this); // A simple way to let game listen to the gamePhase variable.
		this.gamePhase = gamePhase;
	}

	public int getPhaseTime() {
		return phaseTime;
	}

	
	public synchronized void setPhaseTime(int phaseTime) {
		this.phaseTime = phaseTime;
	}

	public LobbyPlayer getAlivePlayer(String playerid) {
		return alivePlayers.get(playerid);
	}

	public void addAlivePlayer(LobbyPlayer alivePlayer) {
		if (alivePlayer.getId() == null)
			return;

		if(deadPlayers.containsKey(alivePlayer.getId()))
			deadPlayers.remove(alivePlayer.getId());
		
		alivePlayers.put(alivePlayer.getId(), alivePlayer);
	}

	public int getAliveCount() {
		return alivePlayers.size();
	}

	public Collection<LobbyPlayer> getAlivePlayers() {
		return alivePlayers.values();
	}
	
	public void addToTeamEvil(LobbyPlayer lobbyPlayer) {
		if(lobbyPlayer == null || lobbyPlayer.getId() == null || teamEvil.containsKey(lobbyPlayer.getId()))
			return;
		
		teamEvil.put(lobbyPlayer.getId(), lobbyPlayer);
	}

	public boolean evilTeamContains(String id) { return teamEvil.containsKey(id); }
	public LobbyPlayer getEvilPlayer(String id) { return teamEvil.get(id); }

	public Collection<LobbyPlayer> getEvilTeam() {
		return teamEvil.values();
	}

	public LobbyPlayer getDeadPlayer(String playerid) {
		return deadPlayers.get(playerid);
	}

	public void addDeadPlayer(LobbyPlayer deadPlayer) {
		if (deadPlayer.getId() == null)
			return;

		alivePlayers.remove(deadPlayer.getId());
		deadPlayers.put(deadPlayer.getId(), deadPlayer);
	}

	public Collection<LobbyPlayer> getDeadPlayers() {
		return deadPlayers.values();
	}

	public void setStartedState(boolean gamestarted) {
		this.gamestarted = gamestarted;
	}

	public String getGameId() {
		return gameid;
	}

	public LobbyPlayer getPlayer(String playerid) {
		return lobbyplayers.get(playerid);
	}

	public int getReadyPlayerCount() {
		return readyPlayerCount;
	}

	public void removePlayer(LobbyPlayer player) {
		if (player.getId() == null || !lobbyplayers.containsKey(player.getId()))
			return;

		if(!gamestarted)
			lobbyplayers.remove(player.getId());
	}

	public int getMinPlayers() {
	    return minPlayers;
    }

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public LobbyPlayer addPlayer(User user, String nickname) {
		if (lobbyplayers.size() >= maxPlayers)
			return null;

		LobbyPlayer playerAlreadyInLobby = lobbyplayers.get(Long.toString(user.getId()));
		
		if(playerAlreadyInLobby != null)
			return playerAlreadyInLobby;
		
		LobbyPlayer lobbyPlayer = new LobbyPlayer(Long.toString(user.getId()), user, this);
		lobbyPlayer.setNickname(nickname);
		lobbyplayers.put(lobbyPlayer.getId(), lobbyPlayer);

		return lobbyPlayer;
	}

	public int getPlayerSize() {
		return lobbyplayers.size();
	}

	public Collection<LobbyPlayer> getPlayers() {
		return lobbyplayers.values();
	}

	public void setReadyPlayerCount(int readyPlayerCount) {
		this.readyPlayerCount = readyPlayerCount;
	}
	
	public Game getGame() {
		return game;
	}
}
