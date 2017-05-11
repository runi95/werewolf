package com.werewolf.services;

import com.werewolf.Messages.LobbyMessage;
import com.werewolf.data.JoinLobbyForm;
import com.werewolf.data.NameDictionaryRepository;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.entities.NameDictionary;
import com.werewolf.entities.User;
import com.werewolf.gameplay.AdvancedMode;
import com.werewolf.gameplay.ChaoticEvil;
import com.werewolf.gameplay.Evil;
import com.werewolf.gameplay.GameModes;
import com.werewolf.gameplay.Good;
import com.werewolf.gameplay.Neutral;
import com.werewolf.gameplay.NeutralEvil;
import com.werewolf.gameplay.RoleInterface;
import com.werewolf.gameplay.roles.Amnesiac;
import com.werewolf.gameplay.roles.Bandit;
import com.werewolf.gameplay.roles.Bard;
import com.werewolf.gameplay.roles.Guard;
import com.werewolf.gameplay.roles.Inquisitor;
import com.werewolf.gameplay.roles.Jester;
import com.werewolf.gameplay.roles.King;
import com.werewolf.gameplay.roles.Knight;
import com.werewolf.gameplay.roles.Marauder;
import com.werewolf.gameplay.roles.Priest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class JoinLobbyServiceImpl implements JoinLobbyService {

	private final HashMap<Long, LobbyPlayer> playerMap = new HashMap<>();
	private final HashMap<String, LobbyEntity> lobbyMap = new HashMap<>();

	@Autowired
	SimpMessagingTemplate simpTemplate;

	@Autowired
	AdvancedMode advancedGameMode;

	@Autowired
	NameDictionaryRepository nameDictionaryRepository;

	@Autowired
	AccountService accountService;

	@Override
	public LobbyPlayer getPlayer(long userid) {
		return playerMap.get(userid);
	}

	// Assumes that there are no games with given id
	@Override
	public LobbyEntity create(JoinLobbyForm joinLobbyForm) {
		String gameid = generateNewGameid();
		LobbyEntity lobbyEntity = new LobbyEntity(gameid);
		lobbyMap.put(gameid, lobbyEntity);

		joinLobbyForm.setGameid(gameid);
		join(joinLobbyForm);

		return lobbyEntity;
	}

	@Override
	public LobbyEntity join(JoinLobbyForm joinLobbyForm) {
		List<LobbyMessage> messageList = new ArrayList<>();

		LobbyPlayer oldLobby = playerMap.get(joinLobbyForm.getUser().getId());
		if (oldLobby != null)
			leave(playerMap.get(joinLobbyForm.getUser().getId()));

		LobbyEntity lobbyEntity = lobbyMap.get(joinLobbyForm.getGameid());
		if (lobbyEntity == null)
			return null;

		if (invalidNickname(joinLobbyForm.getNickname()))
			joinLobbyForm.setNickname(generateNewNickname());

		LobbyPlayer lobbyPlayer = lobbyEntity.addPlayer(joinLobbyForm);
		playerMap.put(joinLobbyForm.getUser().getId(), lobbyPlayer);

		messageList.add(new LobbyMessage("join", lobbyPlayer.getId(), lobbyPlayer.getNickname()));

		if (!messageList.isEmpty())
			broadcastMessage(lobbyPlayer.getLobby().getGameId(), JoinLobbyService.convertObjectToJson(messageList));

		return lobbyEntity;
	}

	// Unsure if this is going to be needed, but keeping it anyway
	@Override
	public void leave(String username) {
		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);

		leave(lobbyPlayer);
	}

	@Override
	public void setReadyStatus(String username, boolean ready) {
		List<LobbyMessage> messageList = new ArrayList<LobbyMessage>();

		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if (lobbyPlayer == null)
			return;

		LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
		if (ready != lobbyPlayer.ready()) {
			lobbyPlayer.setReady(ready);

			if (ready)
				lobbyEntity.setReadyPlayerCount(lobbyEntity.getReadyPlayerCount() + 1);
			else
				lobbyEntity.setReadyPlayerCount(lobbyEntity.getReadyPlayerCount() - 1);
		}

		messageList.add(new LobbyMessage("updatereadystatus", lobbyPlayer.getId(),
				Integer.toString(lobbyEntity.getReadyPlayerCount()), Integer.toString(lobbyEntity.getPlayerSize())));

		if (lobbyEntity.getReadyPlayerCount() == lobbyEntity.getPlayerSize() && lobbyEntity.getPlayerSize() >= 3)
			loadGame(lobbyEntity);

		if (!messageList.isEmpty())
			broadcastMessage(lobbyPlayer.getLobby().getGameId(), JoinLobbyService.convertObjectToJson(messageList));
	}

	@Override
	public LobbyEntity findByGameId(String gameId) {
		return lobbyMap.get(gameId);
	}

	@Override
	public JoinLobbyForm getEditForm(LobbyEntity lobbyEntity) {
		JoinLobbyForm joinLobbyForm = new JoinLobbyForm();
		joinLobbyForm.setGameid(lobbyEntity.getGameId());
		return joinLobbyForm;
	}

	@Override
	public boolean gameidIsPresent(String gameid) {
		return lobbyMap.containsKey(gameid);
	}

	@Override
	public void vote(String username, String voteon, boolean status) {
		LobbyPlayer voter = getPlayerFromUsername(username);
		if (voter == null)
			return;

		LobbyPlayer voteonPlayer = voter.getLobby().getAlivePlayer(voteon);
		if (voteonPlayer == null || (voter.getVoted() != null && voter.getVoted().equals(voteon) && status)
				|| voter.getLobby().getDeadPlayer(voter.getId()) != null || voter.getId().equals(voteon))
			return;

		LobbyPlayer oldVoteTarget = null;
		if (voter.getVoted() != null) {
			oldVoteTarget = voter.getLobby().getAlivePlayer(voter.getVoted());
		}

		lobbyVote(voter.getLobby(), voter, voteonPlayer, oldVoteTarget, status);
	}

	@Override
	public void getPlayers(String username) {
		List<LobbyMessage> messageList = new ArrayList<>();

		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if (lobbyPlayer == null)
			return;

		LobbyEntity lobbyEntity = lobbyPlayer.getLobby();

		if (lobbyEntity != null) {
			for (LobbyPlayer lp : lobbyEntity.getPlayers()) {
				if (lp.getId() == lobbyPlayer.getId())
					messageList.add(new LobbyMessage("owner", lp.getId(), lp.getNickname()));
				else
					messageList.add(new LobbyMessage("join", lp.getId(), lp.getNickname()));
			}
		}

		if (!messageList.isEmpty())
			privateMessage(username, JoinLobbyService.convertObjectToJson(messageList));
	}

	@Override
	public void initializeGame(String username) {
		List<LobbyMessage> messageList = new ArrayList<>();

		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if (lobbyPlayer == null)
			return;

		LobbyEntity lobbyEntity = lobbyPlayer.getLobby();

		if (lobbyEntity != null) {
			lobbyEntity.getAlivePlayers().forEach((lp) -> messageList
					.add(new LobbyMessage("joinalive", lp.getId(), lp.getNickname(), Integer.toString(lp.getVotes()))));
			lobbyEntity.getDeadPlayers().forEach((lp) -> messageList.add(new LobbyMessage("joindead", lp.getId(),
					lp.getNickname(), lp.getRole().getName(), lp.getAlignment())));

			messageList
					.add(new LobbyMessage("role", lobbyPlayer.getRole().getName(), lobbyPlayer.getRole().getAlignment(),
							lobbyPlayer.getRole().getGoal(), lobbyPlayer.getRole().getDescription()));
		}

		if (!messageList.isEmpty())
			privateMessage(username, JoinLobbyService.convertObjectToJson(messageList));
	}

	@Override
	public void nightAction(String username, String target, boolean act) {
		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if (lobbyPlayer == null)
			return;

		LobbyPlayer targetPlayer = lobbyPlayer.getLobby().getAlivePlayer(target);
		if (targetPlayer == null)
			return;

		LobbyPlayer oldTargetPlayer = null;
		if (lobbyPlayer.getTarget() != null) {
			oldTargetPlayer = playerMap.get(lobbyPlayer.getTarget());
		}
		lobbyNightAction(lobbyPlayer.getLobby(), lobbyPlayer, oldTargetPlayer, targetPlayer, act);
	}

	public void getGamePhase(String username) {
		List<LobbyMessage> messageList = new ArrayList<>();

		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if (lobbyPlayer == null)
			return;

		messageList.add(new LobbyMessage("gamephase", lobbyPlayer.getLobby().getPhase()));

		if (!messageList.isEmpty())
			privateMessage(username, JoinLobbyService.convertObjectToJson(messageList));
	}

	public void getRole(String username) {
		List<LobbyMessage> messageList = new ArrayList<>();

		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if (lobbyPlayer == null)
			return;

		if (lobbyPlayer.getRole() == null)
			return;

		messageList
				.add(new LobbyMessage("role", lobbyPlayer.getRole().getName(), lobbyPlayer.getRole().getDescription(),
						lobbyPlayer.getRole().getAlignment(), lobbyPlayer.getRole().getGoal()));

		if (!messageList.isEmpty())
			privateMessage(username, JoinLobbyService.convertObjectToJson(messageList));
	}

	// TODO: Possibly make this failsafe
	private String generateNewGameid() {
		SecureRandom random = new SecureRandom();
		int num = random.nextInt(58786560) + 1679616;
		String generatedGameid = Integer.toUnsignedString(num, 36).toUpperCase();

		if (gameidIsPresent(generatedGameid))
			return generateNewGameid();
		else
			return generatedGameid;
	}

	private boolean invalidNickname(String nickname) {
		if (nickname != null) {
			nickname = nickname.replaceAll("\\s+", "");
			if (nickname.equals(""))
				return true;
			else
				return false;
		} else
			return true;
	}

	private String generateNewNickname() {
		SecureRandom random = new SecureRandom();
		int repositorySize = (int) nameDictionaryRepository.count();
		int num = random.nextInt(repositorySize) + 1;
		NameDictionary name = nameDictionaryRepository.findOne(num);
		num = random.nextInt(repositorySize) + 1;
		NameDictionary surname = nameDictionaryRepository.findOne(num);

		return name.getName() + " " + surname.getName();
	}

	private Collection<LobbyPlayer> createPlayers(Collection<LobbyPlayer> lobbyPlayers, LobbyEntity lobbyEntity) {
		List<RoleInterface> lottery = new ArrayList<>();
		int size = lobbyPlayers.size();
		switch (size) {
		case 15:
			lottery.add(new Bard());
		case 14:
			lottery.add(ChaoticEvil.getRandomChaoticEvil());
		case 13:
			lottery.add(new Guard());
		case 12:
			lottery.add(new Inquisitor());
		case 11:
			lottery.add(Neutral.getRandomNeutral());
		case 10:
			lottery.add(new Knight());
		case 9:
			lottery.add(RoleInterface.getRandomRole());
		case 8:
			lottery.add(Evil.getRandomEvil());
		case 7:
			lottery.add(new Marauder());
			lottery.add(new Bandit());
			lottery.add(NeutralEvil.getRandomNeutralEvil());
			lottery.add(Good.getRandomGood());
			lottery.add(new Priest());
			lottery.add(new Inquisitor());
			lottery.add(new King());
			break;
		case 6:
			lottery.add(new Marauder());
			lottery.add(new Bandit());
			lottery.add(RoleInterface.getRandomRole());
			lottery.add(Good.getRandomGood());
			lottery.add(new Inquisitor());
			lottery.add(new King());
			break;
		case 5:
			lottery.add(new Marauder());
			lottery.add(new Bandit());
			lottery.add(Good.getRandomGood());
			lottery.add(new Amnesiac());
			lottery.add(new King());
			break;
		case 4:
			lottery.add(new Marauder());
			lottery.add(new Jester());
			lottery.add(new Priest());
			lottery.add(new King());
			break;
		case 3:
			lottery.add(new Marauder());
		case 2:
			lottery.add(new Priest());
		case 1:
			lottery.add(new Marauder());
		}

		for (LobbyPlayer lobbyPlayer : lobbyPlayers) {
			Random random = new Random();
			int sz = lottery.size();
			int rng = random.nextInt(sz);
			RoleInterface role = lottery.get(rng);
			lottery.remove(rng);

			lobbyPlayer.setRole(role);
			lobbyPlayer.setAlignment(role.getAlignment());
			System.out.println(
					"Player (" + lobbyPlayer.getNickname() + ") became " + role.getName() + " with rnd number " + rng);
		}

		return lobbyPlayers;
	}

	private void leave(LobbyPlayer lobbyPlayer) {
		List<LobbyMessage> messageList = new ArrayList<LobbyMessage>();

		if (lobbyPlayer == null)
			return;

		LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
		lobbyEntity.removePlayer(lobbyPlayer);
		playerMap.remove(lobbyPlayer.getUser().getId());
		if (lobbyEntity.getPlayerSize() == 0) {
			lobbyMap.remove(lobbyEntity.getGameId());
		} else {
			messageList.add(new LobbyMessage("leave", lobbyPlayer.getId(), lobbyPlayer.getNickname()));
		}

		if (!messageList.isEmpty())
			broadcastMessage(lobbyPlayer.getLobby().getGameId(), JoinLobbyService.convertObjectToJson(messageList));
	}

	private LobbyPlayer getPlayerFromUsername(String username) {
		User loggedinuser = accountService.findByUsername(username);
		LobbyPlayer lobbyPlayer = getPlayer(loggedinuser.getId());

		return lobbyPlayer;
	}

	private void loadGame(LobbyEntity lobbyEntity) {
		if (lobbyEntity == null)
			return;

		List<LobbyMessage> messageList = new ArrayList<LobbyMessage>();

		if (lobbyEntity.getReadyPlayerCount() != lobbyEntity.getReadyPlayerCount())
			return;

		Collection<LobbyPlayer> lobbyPlayersWithRoles = createPlayers(lobbyEntity.getPlayers(), lobbyEntity);
		lobbyPlayersWithRoles.forEach((p) -> lobbyEntity.addAlivePlayer(p));

		lobbyEntity.setGameMode(GameModes.AdvancedMode);
		initializeLobby(lobbyEntity);

		messageList.add(new LobbyMessage("lobbyready"));

		if (!messageList.isEmpty())
			broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));
	}

	private void initializeLobby(LobbyEntity lobbyEntity) {
		switch (lobbyEntity.getGameMode()) {
		case AdvancedMode:
			advancedGameMode.initalizeGame(lobbyEntity);
			break;
		default:
			advancedGameMode.initalizeGame(lobbyEntity);
			break;
		}
	}

	private void lobbyNightAction(LobbyEntity lobbyEntity, LobbyPlayer acter, LobbyPlayer oldTarget, LobbyPlayer target,
			boolean act) {
		switch (lobbyEntity.getGameMode()) {
		case AdvancedMode:
			advancedGameMode.nightAction(lobbyEntity, acter, oldTarget, target, act);
			break;
		default:
			advancedGameMode.nightAction(lobbyEntity, acter, oldTarget, target, act);
			break;
		}
	}

	private void lobbyVote(LobbyEntity lobbyEntity, LobbyPlayer voter, LobbyPlayer voteTarget,
			LobbyPlayer oldVoteTarget, boolean status) {

		System.out.println("debug 4");
		switch (lobbyEntity.getGameMode()) {
		case AdvancedMode:
			advancedGameMode.vote(lobbyEntity, voter, voteTarget, oldVoteTarget, status);
			break;
		default:
			advancedGameMode.vote(lobbyEntity, voter, voteTarget, oldVoteTarget, status);
			break;
		}
	}

	private void broadcastMessage(String gameid, String message) {
		simpTemplate.convertAndSend("/action/broadcast/" + gameid, message);
	}

	private void privateMessage(String user, String message) {
		simpTemplate.convertAndSendToUser(user, "/action/private", message);
	}
}
