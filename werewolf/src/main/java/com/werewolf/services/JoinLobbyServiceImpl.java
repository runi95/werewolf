package com.werewolf.services;


import com.werewolf.Messages.LobbyMessage;
import com.werewolf.data.JoinLobbyForm;
import com.werewolf.data.LobbyEntityRepository;
import com.werewolf.data.LobbyPlayerRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Repository
public class JoinLobbyServiceImpl implements JoinLobbyService {
	
	private final HashMap<String, HashMap<String, EmulationCharacter>> gameMap = new HashMap<>();
	
    @Autowired
    LobbyEntityRepository lobbyEntityRepository;

    @Autowired
    LobbyPlayerRepository lobbyPlayerRepository;
    
    @Autowired
    NameDictionaryRepository nameDictionaryRepository;
    
    @Override
    public void dropTable() {
    	lobbyPlayerRepository.deleteAll();
    	lobbyEntityRepository.deleteAll();
    }

    // Assumes that there are no games with given id
    @Override
    public LobbyEntity create(JoinLobbyForm joinLobbyForm) {
        LobbyEntity lobbyEntity = new LobbyEntity();

        String gameid = generateNewGameid();
        lobbyEntity.setGameid(gameid);
        
        lobbyEntityRepository.save(lobbyEntity);
        
        joinLobbyForm.setGameid(gameid);
        join(joinLobbyForm);
        
        return lobbyEntity;
    }

    @Override
    public LobbyEntity join(JoinLobbyForm joinLobbyForm) {
        if(lobbyPlayerRepository.findByUser(joinLobbyForm.getUser()).isPresent())
            leave(lobbyPlayerRepository.findByUser(joinLobbyForm.getUser()).get());

        LobbyEntity lobbyEntity = lobbyEntityRepository.findByGameid(joinLobbyForm.getGameid()).orElseThrow(() -> new IllegalArgumentException("A lobby with that gameId does not exist"));

        if(invalidNickname(joinLobbyForm.getNickname()))
        	joinLobbyForm.setNickname(generateNewNickname());
        
        LobbyPlayer lobbyPlayer = new LobbyPlayer();
        lobbyPlayer.setNickname(joinLobbyForm.getNickname());
        lobbyPlayer.setUser(joinLobbyForm.getUser());

        lobbyEntity.addPlayer(lobbyPlayer);
        
        lobbyEntityRepository.save(lobbyEntity);
        
        return lobbyEntity;
    }
    
    @Override
    public void leave(LobbyPlayer lobbyPlayer) {
        LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
        lobbyEntity.getPlayers().remove(lobbyPlayer);
        
        lobbyEntityRepository.save(lobbyEntity);
    }
    
    @Override
    public Integer setReadyStatus(LobbyPlayer lobbyPlayer, boolean ready) {
    	if(lobbyPlayer == null)
    		return null;
    	
    	lobbyPlayer.setReady(ready);
    	lobbyPlayerRepository.save(lobbyPlayer);
    	
    	LobbyEntity lobbyEntity = lobbyPlayer.getLobby();
    	if(ready)
    		lobbyEntity.setReadyPlayerCount(lobbyEntity.getReadyPlayerCount() + 1);
    	else
    		lobbyEntity.setReadyPlayerCount(lobbyEntity.getReadyPlayerCount() - 1);
    	lobbyEntityRepository.save(lobbyEntity);
    	
    	return lobbyEntity.getReadyPlayerCount();
    }
    
    @Override
    public Integer getPlayerCount(LobbyPlayer lobbyPlayer) {
    	if(lobbyPlayer == null)
    		return null;
    	
    	return lobbyPlayer.getLobby().getPlayers().size();
    }

    @Override
    public LobbyEntity findByGameId(String gameId) {
        return lobbyEntityRepository.findByGameid(gameId).orElseThrow(() -> new IllegalArgumentException("A lobby with that gameId does not exist"));
    }

    @Override
    @Transactional
    public LobbyEntity findById(long id) {
        LobbyEntity lobby = lobbyEntityRepository.findOne(id);
        if(lobby != null)
            return lobby;
        else {
            throw new IllegalArgumentException("A lobby with that id does not exist");
        }
    }

    @Override
    public JoinLobbyForm getEditForm(LobbyEntity lobbyEntity) {
        JoinLobbyForm joinLobbyForm = new JoinLobbyForm();
        joinLobbyForm.setGameid(lobbyEntity.getGameId());
        return joinLobbyForm;
    }

    @Override
    public boolean gameidIsPresent(String gameid) {
        return lobbyEntityRepository.findByGameid(gameid).isPresent();
    }
    
    @Override
    public void loadGame(LobbyEntity lobbyEntity) {
    	lobbyEntity.setStartedState(true);
		lobbyEntityRepository.save(lobbyEntity); // Make sure others can't join
													// while game initializes

		gameMap.put(lobbyEntity.getGameId(), new HashMap<>());
		
		lobbyEntity.setGameid(lobbyEntity.getGameId());
		Set<LobbyPlayer> lobbyPlayersWithRoles = createPlayers(lobbyEntity.getPlayers(), lobbyEntity);
		lobbyEntity.setAlivePlayers(lobbyPlayersWithRoles);
		lobbyEntityRepository.save(lobbyEntity);
    }
    
    @Override
    public int vote(LobbyPlayer voter, String voteon) {
    	LobbyPlayer voteonPlayer = lobbyPlayerRepository.findById(voteon);
    	
    	if(voteonPlayer == null)
    		throw new IllegalArgumentException("Tried to vote on nonexisting player!");
    	else if(voter.getVoted() != null || voter.getVoted().equals(voteon) || voter.getLobby().getDeadPlayers().contains(voter))
    		return voteonPlayer.getVotes();
    	
    	if(voter.getVoted() != null) {
    		LobbyPlayer oldVoteonPlayer = lobbyPlayerRepository.findById(voter.getVoted());
    		oldVoteonPlayer.setVotes(oldVoteonPlayer.getVotes() - 1);
    		lobbyPlayerRepository.save(oldVoteonPlayer);
    	}
    	
    	voteonPlayer.setVotes(voteonPlayer.getVotes() + 1);
    	voter.setVoted(voteon);
    	lobbyPlayerRepository.save(voteonPlayer);
    	lobbyPlayerRepository.save(voter);
    	
    	checkVotes(voteonPlayer);
    	return voteonPlayer.getVotes();
    }
    
    @Override
    public int removeVote(LobbyPlayer voter, String voteon) {
    	LobbyPlayer voteonPlayer = lobbyPlayerRepository.findById(voteon);
    	
    	if(voteonPlayer == null)
    		throw new IllegalArgumentException("Tried to remove vote on nonexisting player!");
    	else if(!voter.getVoted().equals(voteon))
    		return voteonPlayer.getVotes();
    	
    	voteonPlayer.setVotes(voteonPlayer.getVotes() - 1);
    	voter.setVoted(null);
    	lobbyPlayerRepository.save(voteonPlayer);
    	lobbyPlayerRepository.save(voter);
    	
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
    
    private Set<LobbyPlayer> createPlayers(Set<LobbyPlayer> lobbyPlayers, LobbyEntity lobbyEntity) {
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
    	
    	if(latestVotedOn.getVotes() >= (Math.ceil(lobby.getAlivePlayers().size()/2.0))) {
    		lobby.getAlivePlayers().remove(latestVotedOn);
    		lobby.getDeadPlayers().add(latestVotedOn);
    		
    		lobbyEntityRepository.save(lobby);
    		
    		List<LobbyMessage> lobbyMessages = new ArrayList<>();
    		lobbyMessages.add(new LobbyMessage("lynch", latestVotedOn.getId(), latestVotedOn.getNickname(), latestVotedOn.getRole(), latestVotedOn.getAlignment()));
//    		simpTemplate.convertAndSend("/action/broadcast/" + lobby.getGameId(), StompMessageController.convertObjectToJson(lobbyMessages));
    	}
    }
}
