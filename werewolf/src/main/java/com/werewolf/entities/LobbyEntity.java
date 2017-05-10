package com.werewolf.entities;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.werewolf.data.JoinLobbyForm;
import com.werewolf.gameplay.GameModes;

public class LobbyEntity {

	// ID used to join this lobby with
	private String gameid;

	// How many rounds have this lobby been through?
	private int rounds = 0;

	private String gamePhase = "lobby";

	// How much time is left on this phase?
	private int phaseTime = 0;

	private Map<String, LobbyPlayer> lobbyplayers = new HashMap<String, LobbyPlayer>();
	private Map<String, LobbyPlayer> alivePlayers = new HashMap<String, LobbyPlayer>();
	private Map<String, LobbyPlayer> deadPlayers = new HashMap<String, LobbyPlayer>();

	// Amount of players that has clicked ready during the lobby phase
	private int readyPlayerCount = 0;

	// True if the lobby is in-game and false otherwise
	private boolean gamestarted = false;
	
	private GameModes game;

	// Keep track of unused id's
	private LinkedList<String> nextPlayerId = new LinkedList<>(Arrays.asList(new String[] { "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));

	public LobbyEntity(String gameid) {
		this.gameid = gameid;
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

	public String getPhase() {
		return gamePhase;
	}

	public synchronized void setPhase(String gamePhase) {
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

		alivePlayers.put(alivePlayer.getId(), alivePlayer);
	}

	public int getAliveCount() {
		return alivePlayers.size();
	}

	public Collection<LobbyPlayer> getAlivePlayers() {
		return alivePlayers.values();
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

		lobbyplayers.remove(player.getId());
		nextPlayerId.add(player.getId());
	}

	public LobbyPlayer addPlayer(JoinLobbyForm joinForm) {
		if (nextPlayerId.isEmpty())
			return null;

		LobbyPlayer lobbyPlayer = new LobbyPlayer(nextPlayerId.getFirst(), joinForm.getUser(), this);
		nextPlayerId.removeFirst();
		lobbyPlayer.setNickname(joinForm.getNickname());
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

	public void setGameMode(GameModes game) {
		this.game = game;
	}
	
	public GameModes getGameMode() {
		return game;
	}
}
