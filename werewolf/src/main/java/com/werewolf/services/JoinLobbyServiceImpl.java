package com.werewolf.services;

import com.werewolf.Messages.LobbyMessage;
import com.werewolf.data.JoinLobbyForm;
import com.werewolf.data.NameDictionaryRepository;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.entities.NameDictionary;
import com.werewolf.entities.User;
import com.werewolf.gameplay.ChaoticEvil;
import com.werewolf.gameplay.EmulationCharacter;
import com.werewolf.gameplay.Evil;
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
import org.springframework.stereotype.Repository;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Repository
public class JoinLobbyServiceImpl implements JoinLobbyService {

	private final HashMap<Long, LobbyPlayer> playerMap = new HashMap<>();
	private final HashMap<String, LobbyEntity> lobbyMap = new HashMap<>();
	private final HashMap<String, HashMap<String, EmulationCharacter>> gameMap = new HashMap<>();

	@Autowired
	NameDictionaryRepository nameDictionaryRepository;
	
	@Autowired
	AccountService accountService;

	@Override
	public List<LobbyMessage> join(String username) {
		List<LobbyMessage> messageList = new ArrayList<>();
		
		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if(lobbyPlayer == null)
			return messageList;
		
		messageList.add(new LobbyMessage("join", lobbyPlayer.getId(), lobbyPlayer.getNickname()));
		
		return messageList;
	}
	
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

		return lobbyEntity;
	}

	// Unsure if this is going to be needed, but keeping it anyway
	@Override
	public List<LobbyMessage> leave(String username) {
		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		
		return leave(lobbyPlayer);
	}
	
	@Override
	public List<LobbyMessage> leave(LobbyPlayer lobbyPlayer) {
		List<LobbyMessage> messageList = new ArrayList<LobbyMessage>();
		
		if(lobbyPlayer == null)
			return messageList;
		
		LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
		lobbyEntity.removePlayer(lobbyPlayer);
		playerMap.remove(lobbyPlayer.getUser().getId());
		if (lobbyEntity.getPlayerSize() == 0) {
			lobbyMap.remove(lobbyEntity.getGameId());
		} else {
			messageList.add(new LobbyMessage("leave", lobbyPlayer.getId(), lobbyPlayer.getNickname()));
		}

		return messageList;
	}

	@Override
	public List<LobbyMessage> setReadyStatus(String username, boolean ready) {
		List<LobbyMessage> messageList = new ArrayList<LobbyMessage>();
		
		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if(lobbyPlayer == null)
			return messageList;

		lobbyPlayer.setReady(ready);

		LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
		if (ready)
			lobbyEntity.setReadyPlayerCount(lobbyEntity.getReadyPlayerCount() + 1);
		else
			lobbyEntity.setReadyPlayerCount(lobbyEntity.getReadyPlayerCount() - 1);

		messageList.add(new LobbyMessage("updatereadystatus", lobbyPlayer.getId(),
				Integer.toString(lobbyEntity.getReadyPlayerCount()),
				Integer.toString(lobbyEntity.getReadyPlayerCount())));

		if (lobbyEntity.getReadyPlayerCount() == lobbyEntity.getReadyPlayerCount())
			loadGame(lobbyPlayer.getLobby());

		return messageList;
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

	private void loadGame(LobbyEntity lobbyEntity) {
		lobbyEntity.setStartedState(true);

		gameMap.put(lobbyEntity.getGameId(), new HashMap<>());

		Collection<LobbyPlayer> lobbyPlayersWithRoles = createPlayers(lobbyEntity.getPlayers(), lobbyEntity);
		lobbyPlayersWithRoles.forEach((p) -> lobbyEntity.addAlivePlayer(p));
	}

	@Override
    public List<LobbyMessage> vote(String username, String voteon, boolean vote) {
    	List<LobbyMessage> messageList = new ArrayList<>();
    	
    	LobbyPlayer voter = getPlayerFromUsername(username);
		if(voter == null)
			return messageList;
    	
    	LobbyPlayer voteonPlayer = voter.getLobby().getAlivePlayer(voteon);
    	
    	if(voteonPlayer == null || (voter.getVoted() != null && voter.getVoted().equals(voteon)) || voter.getLobby().getDeadPlayer(voter.getId()) != null || voter.getId().equals(voteon))
    		return messageList;
    	
    	if(vote) {
    		if(voter.getVoted() != null) {
    			LobbyPlayer oldVoteonPlayer = playerMap.get(voter.getVoted());
    			oldVoteonPlayer.setVotes(oldVoteonPlayer.getVotes() - 1);
    		}
    	
    		voteonPlayer.setVotes(voteonPlayer.getVotes() + 1);
    		voter.setVoted(voteon);
    	
    		checkVotes(voteonPlayer);
    		
    		messageList.add(new LobbyMessage("updatevotestatus", voter.getId(), voteon, Integer.toString(voteonPlayer.getVotes())));
    	} else {
    		voteonPlayer.setVotes(voteonPlayer.getVotes() - 1);
    		voter.setVoted(null);
    	}
    	
    	return messageList;
    }
	
	@Override
	public List<LobbyMessage> getPlayers(String username) {
		List<LobbyMessage> messageList = new ArrayList<>();
		
		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if(lobbyPlayer == null)
			return messageList;
		
		LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
		
		if(lobbyEntity != null) {
			for (LobbyPlayer lp : lobbyEntity.getPlayers()) {
				if (lp.getId() == lobbyPlayer.getId())
					messageList.add(new LobbyMessage("owner", lp.getId(), lp.getNickname()));
				else
					messageList.add(new LobbyMessage("join", lp.getId(), lp.getNickname()));
			}
		}
		
		return messageList;
	}
	
	@Override
	public List<LobbyMessage> gameRequest(String username) {
		List<LobbyMessage> messageList = new ArrayList<>();
		
		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if(lobbyPlayer == null)
			return messageList;
		
		if(lobbyPlayer.getLobby().getStartedState()) {
			messageList.add(new LobbyMessage("gamerequestgranted", lobbyPlayer.getId(), lobbyPlayer.getNickname()));
		}else{
			messageList.add(new LobbyMessage("gamerequestgranted", lobbyPlayer.getId(), lobbyPlayer.getNickname()));
		}
		
		return messageList;
	}
	
	@Override
	public List<LobbyMessage> initializeGame(String username) {
		List<LobbyMessage> messageList = new ArrayList<>();
		
		LobbyPlayer lobbyPlayer = getPlayerFromUsername(username);
		if(lobbyPlayer == null)
			return messageList;
		
		LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
		
		if(lobbyEntity != null) {
			lobbyEntity.getAlivePlayers().forEach((lp) -> messageList.add(new LobbyMessage("joinalive", lp.getId(), lp.getNickname(), Integer.toString(lp.getVotes()))));
			lobbyEntity.getDeadPlayers().forEach((lp) -> messageList.add(new LobbyMessage("joindead", lp.getId(), lp.getNickname(), lp.getRole(), lp.getAlignment()))); 
		}
		
		return messageList;
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
			lottery.add(new Marauder());
		case 1:
			lottery.add(new Marauder());
		}

		for (LobbyPlayer lobbyPlayer : lobbyPlayers) {
			Random random = new Random();
			int sz = lottery.size();
			int rng = random.nextInt(sz);
			RoleInterface role = lottery.get(rng);
			lottery.remove(rng);

			lobbyPlayer.setRole(role.getName());
			lobbyPlayer.setAlignment(role.getAlignment());

			gameMap.get(lobbyEntity.getGameId()).put(lobbyPlayer.getId(),
					new EmulationCharacter(lobbyPlayer.getId(), role));
		}

		return lobbyPlayers;
	}

	private void checkVotes(LobbyPlayer latestVotedOn) {
		LobbyEntity lobby = latestVotedOn.getLobby();

		if (latestVotedOn.getVotes() >= (Math.ceil(lobby.getAliveCount() / 2.0))) {
			lobby.addDeadPlayer(latestVotedOn);

			List<LobbyMessage> lobbyMessages = new ArrayList<>();
			lobbyMessages.add(new LobbyMessage("lynch", latestVotedOn.getId(), latestVotedOn.getNickname(),
					latestVotedOn.getRole(), latestVotedOn.getAlignment()));
		}
	}
	
	private LobbyPlayer getPlayerFromUsername(String username) {
		User loggedinuser = accountService.findByUsername(username);
		LobbyPlayer lobbyPlayer = getPlayer(loggedinuser.getId());

		return lobbyPlayer;
	}
}
