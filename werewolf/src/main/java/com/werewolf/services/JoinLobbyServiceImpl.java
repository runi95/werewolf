package com.werewolf.services;


import com.werewolf.Messages.LobbyMessage;
import com.werewolf.data.JoinLobbyForm;
import com.werewolf.data.NameDictionaryRepository;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.entities.NameDictionary;
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
        if(oldLobby != null)
            leave(playerMap.get(joinLobbyForm.getUser().getId()));

        LobbyEntity lobbyEntity = lobbyMap.get(joinLobbyForm.getGameid());
        if(lobbyEntity == null)
        	return null;

        if(invalidNickname(joinLobbyForm.getNickname()))
        	joinLobbyForm.setNickname(generateNewNickname());
        
        LobbyPlayer lobbyPlayer = lobbyEntity.addPlayer(joinLobbyForm);
        playerMap.put(joinLobbyForm.getUser().getId(), lobbyPlayer);
        
        return lobbyEntity;
    }
    
    @Override
    public void leave(LobbyPlayer lobbyPlayer) {
        LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
        lobbyEntity.removePlayer(lobbyPlayer);
        playerMap.remove(lobbyPlayer.getUser().getId());
        if(lobbyEntity.getPlayerSize() == 0) {
        	lobbyMap.remove(lobbyEntity.getGameId());
        }
    }
    
    @Override
    public Integer setReadyStatus(LobbyPlayer lobbyPlayer, boolean ready) {
    	if(lobbyPlayer == null)
    		return null;
    	
    	lobbyPlayer.setReady(ready);
    	
    	LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
    	if(ready)
    		lobbyEntity.setReadyPlayerCount(lobbyEntity.getReadyPlayerCount() + 1);
    	else
    		lobbyEntity.setReadyPlayerCount(lobbyEntity.getReadyPlayerCount() - 1);
    	
    	return lobbyEntity.getReadyPlayerCount();
    }
    
    @Override
    public Integer getPlayerCount(LobbyPlayer lobbyPlayer) {
    	if(lobbyPlayer == null)
    		return null;
    	
    	return lobbyPlayer.getLobby().getPlayerSize();
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
    public void loadGame(LobbyEntity lobbyEntity) {
    	lobbyEntity.setStartedState(true);

		gameMap.put(lobbyEntity.getGameId(), new HashMap<>());
		
		Collection<LobbyPlayer> lobbyPlayersWithRoles = createPlayers(lobbyEntity.getPlayers(), lobbyEntity);
		lobbyPlayersWithRoles.forEach((p) -> lobbyEntity.addAlivePlayer(p));
    }
    
    @Override
    public int vote(LobbyPlayer voter, String voteon) {
    	LobbyPlayer voteonPlayer = voter.getLobby().getAlivePlayer(voteon);
    	
    	if(voteonPlayer == null)
    		throw new IllegalArgumentException("Tried to vote on nonexisting player!");
    	else if((voter.getVoted() != null && voter.getVoted().equals(voteon)) || voter.getLobby().getDeadPlayer(voter.getId()) != null || voter.getId().equals(voteon))
    		return voteonPlayer.getVotes();
    	
    	if(voter.getVoted() != null) {
    		LobbyPlayer oldVoteonPlayer = playerMap.get(voter.getVoted());
    		oldVoteonPlayer.setVotes(oldVoteonPlayer.getVotes() - 1);
    	}
    	
    	voteonPlayer.setVotes(voteonPlayer.getVotes() + 1);
    	voter.setVoted(voteon);
    	
    	checkVotes(voteonPlayer);
    	return voteonPlayer.getVotes();
    }
    
    @Override
    public int removeVote(LobbyPlayer voter, String voteon) {
    	LobbyPlayer voteonPlayer = voter.getLobby().getAlivePlayer(voteon);
    	
    	if(voteonPlayer == null)
    		throw new IllegalArgumentException("Tried to remove vote on nonexisting player!");
    	else if(!voter.getVoted().equals(voteon))
    		return voteonPlayer.getVotes();
    	
    	voteonPlayer.setVotes(voteonPlayer.getVotes() - 1);
    	voter.setVoted(null);
    	
    	return voteonPlayer.getVotes();
    }

    // TODO: Possibly make this failsafe
    private String generateNewGameid() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(58786560) + 1679616;
        String generatedGameid = Integer.toUnsignedString(num, 36).toUpperCase();

        if(gameidIsPresent(generatedGameid))
            return generateNewGameid();
        else return generatedGameid;
    }
    
    private boolean invalidNickname(String nickname) {
    	if(nickname != null) {
    		nickname = nickname.replaceAll("\\s+","");
    		if(nickname.equals(""))
    			return true;
    		else 
    			return false;
    	}else
    		return true;
    }
    
    private String generateNewNickname() {
    	SecureRandom random = new SecureRandom();
    	int repositorySize = (int)nameDictionaryRepository.count();
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

			gameMap.get(lobbyEntity.getGameId()).put(lobbyPlayer.getId(), new EmulationCharacter(lobbyPlayer.getId(), role));
		}
		
		return lobbyPlayers;
	}
    
    private void checkVotes(LobbyPlayer latestVotedOn) {
    	LobbyEntity lobby = latestVotedOn.getLobby();
    	
    	if(latestVotedOn.getVotes() >= (Math.ceil(lobby.getAliveCount()/2.0))) {
    		lobby.addDeadPlayer(latestVotedOn);
    		
    		List<LobbyMessage> lobbyMessages = new ArrayList<>();
    		lobbyMessages.add(new LobbyMessage("lynch", latestVotedOn.getId(), latestVotedOn.getNickname(), latestVotedOn.getRole(), latestVotedOn.getAlignment()));
    	}
    }
}
