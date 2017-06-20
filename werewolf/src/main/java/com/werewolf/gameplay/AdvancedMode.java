package com.werewolf.gameplay;

import com.werewolf.Messages.LobbyMessage;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.services.JoinLobbyService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdvancedMode extends GameModeMasterClass {

	@Override
	public String getName() {
		return "Advanced";
	}
	
	@Override
	public void initalizeGame(LobbyEntity lobbyEntity) {
		if(lobbyEntity.getStartedState())
			return;
		else {
			lobbyEntity.setStartedState(true);
			initializeGameWait(lobbyEntity, "nightphase");
		}
	}
	
	@Override
	public void setRoles(LobbyEntity lobbyEntity) {
		Collection<LobbyPlayer> lobbyPlayers = lobbyEntity.getPlayers();
		List<RoleInterface> lottery = new ArrayList<>();
		int size = lobbyPlayers.size();
		switch (size) {
		case 15:
			lottery.add(Roles.Bard);
		case 14:
			lottery.add(Alignments.ChaoticEvil.getRandomRoleFromThisAlignment());
		case 13:
			lottery.add(Roles.Guard);
		case 12:
			lottery.add(Roles.Inquisitor);
		case 11:
			lottery.add(Alignments.Neutral.getRandomRoleFromThisAlignment());
		case 10:
			lottery.add(Roles.Knight);
		case 9:
			lottery.add(RoleInterface.getRandomRole());
		case 8:
			lottery.add(Alignments.Evil.getRandomRoleFromThisAlignment());
		case 7:
			lottery.add(Roles.Priest);
		case 6:
			lottery.add(Roles.Marauder);
			lottery.add(Roles.Bandit);
			lottery.add(RoleInterface.getRandomRole());
			lottery.add(Alignments.Good.getRandomRoleFromThisAlignment());
			lottery.add(Roles.Inquisitor);
			lottery.add(Roles.King);
			break;
		case 5:
			lottery.add(Roles.Marauder);
			lottery.add(Roles.Bandit);
			lottery.add(Alignments.Good.getRandomRoleFromThisAlignment());
			lottery.add(Roles.Amnesiac);
			lottery.add(Roles.King);
			break;
		case 4:
			lottery.add(Roles.Marauder);
			lottery.add(Roles.Jester);
			lottery.add(Roles.Priest);
			lottery.add(Roles.King);
			break;
		case 3:
			lottery.add(Roles.Bard);
		case 2:
			lottery.add(Roles.Amnesiac);
		case 1:
			lottery.add(Roles.Marauder);
		}

		for (LobbyPlayer lobbyPlayer : lobbyPlayers) {
			Random random = new Random();
			int sz = lottery.size();
			int rng = random.nextInt(sz);
			RoleInterface role = lottery.get(rng);
			lottery.remove(rng);

			lobbyPlayer.setRole(role);
			lobbyPlayer.setAlignment(role.getAlignment().getAlignmentName());
			if (role.getAlignment() == Alignments.Evil)
				lobbyEntity.addToTeamEvil(lobbyPlayer);
		}
	}
	
	@Override
	public void nightAction(LobbyEntity lobbyEntity, LobbyPlayer acter, LobbyPlayer oldTarget, LobbyPlayer target, boolean act) {
		if(!lobbyEntity.getPhase().equals("nightphase"))
			return;
		
		List<LobbyMessage> messageList = new ArrayList<>();
		
		if(oldTarget != null) {
			messageList.add(new LobbyMessage("unnightaction", oldTarget.getId()));
		}
		
		if(act) {
			acter.setTarget(target.getId());
			messageList.add(new LobbyMessage("nightaction", target.getId()));
		} else {
			acter.setTarget(null);
			messageList.add(new LobbyMessage("unnightaction", target.getId()));
		}
		
		if(!messageList.isEmpty())
			privateMessage(acter.getUser().getUsername(), JoinLobbyService.convertObjectToJson(messageList));
	}
	
	@Override
	public void vote(LobbyEntity lobbyEntity, LobbyPlayer voter, LobbyPlayer voteTarget, LobbyPlayer oldVoteTarget, boolean status) {
		if(!lobbyEntity.getPhase().equals("dayphase"))
			return;
		
		List<LobbyMessage> messageList = new ArrayList<>();
		
    	if(status) {
    		if(oldVoteTarget != null) {
    			oldVoteTarget.setVotes(oldVoteTarget.getVotes() - 1);
    			messageList.add(new LobbyMessage("updatevotestatus", voter.getId(), oldVoteTarget.getId(), Integer.toString(oldVoteTarget.getVotes()), "-"));
    		}
    		
    		voteTarget.setVotes(voteTarget.getVotes() + 1);
    		voter.setVoted(voteTarget.getId());
    	
    		if(!checkVotes(voter, lobbyEntity, voteTarget))
    			messageList.add(new LobbyMessage("updatevotestatus", voter.getId(), voteTarget.getId(), Integer.toString(voteTarget.getVotes()), "+"));
    	} else {
    		if(voter.getVoted() != null && voter.getVoted().equals(voteTarget.getId())) {
    			voteTarget.setVotes(voteTarget.getVotes() - 1);
    			voter.setVoted(null);
    		}
    		
    		messageList.add(new LobbyMessage("updatevotestatus", voter.getId(), voteTarget.getId(), Integer.toString(voteTarget.getVotes()), "-"));
    	}
    	
    	if(!messageList.isEmpty())
			broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));
	}
	
	private boolean checkVotes(LobbyPlayer voter, LobbyEntity lobbyEntity, LobbyPlayer latestVotedOn) {
		List<LobbyMessage> messageList = new ArrayList<>();
		LobbyEntity lobby = latestVotedOn.getLobby();

		if (latestVotedOn.getVotes() > (Math.floor(lobby.getAliveCount() / 2.0))) {
			lobby.addDeadPlayer(latestVotedOn);

			messageList.add(new LobbyMessage("lynch", latestVotedOn.getId(), latestVotedOn.getNickname(),
					latestVotedOn.getRole().getName(), latestVotedOn.getAlignment()));
			
			if(lobby.getPhaseTime() > 3)
				lobby.setPhaseTime(3);
//			waitPhase(lobbyEntity, "nightphase");
		} else if (voter.getRole() == Roles.King) {
			lobby.addDeadPlayer(latestVotedOn);

			messageList.add(new LobbyMessage("kinglynch", latestVotedOn.getId(), latestVotedOn.getNickname(),
					latestVotedOn.getRole().getName(), latestVotedOn.getAlignment()));
			
			if(latestVotedOn.getRole() == Roles.Jester) {
				new LobbyMessage("jesterkill", voter.getId(), voter.getNickname(), voter.getRole().getName(), voter.getAlignment());
			}
			
			if(lobby.getPhaseTime() > 3)
				lobby.setPhaseTime(3);
		}
		
		if(!messageList.isEmpty()) {
			broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));
			return true;
		} else 
			return false;
	}
	
	private void dayPhase(LobbyEntity lobbyEntity) {
		List<LobbyMessage> messageList = new ArrayList<>();
		lobbyEntity.setPhase("dayphase");
		
		messageList.add(new LobbyMessage("dayphase"));
		
		if(!messageList.isEmpty())
			broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));
		
		startDay(lobbyEntity);
	}
	
	private void nightPhase(LobbyEntity lobbyEntity) {
		List<LobbyMessage> messageList = new ArrayList<>();
		lobbyEntity.setPhase("nightphase");
		
		messageList.add(new LobbyMessage("nightphase"));
		
		if(!messageList.isEmpty())
			broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));
		
		startNight(lobbyEntity);
	}
	
	private void waitPhase(LobbyEntity lobbyEntity, String nextPhase) {
		List<LobbyMessage> messageList = new ArrayList<>();
		
		lobbyEntity.setPhase("waitphase");
		lobbyEntity.setPhaseTime(3);
		
		messageList.add(new LobbyMessage("waitphase"));
		
		if(!messageList.isEmpty())
			broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));
		
		if(!checkWinCondition(lobbyEntity))
			startWaitingPhase(lobbyEntity, nextPhase);
	}
	
	private void initializeGameWait(LobbyEntity lobbyEntity, String nextPhase) {
		List<LobbyMessage> messageList = new ArrayList<>();
		
		lobbyEntity.setPhase("waitphase");
		lobbyEntity.setPhaseTime(3);
		
		messageList.add(new LobbyMessage("waitphase"));
		
		if(!messageList.isEmpty())
			broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));
		
		if(!checkWinCondition(lobbyEntity))
			startWaitingPhase(lobbyEntity, nextPhase);
	}
	
	private boolean checkWinCondition(LobbyEntity lobbyEntity) {
		LinkedList<LobbyPlayer> goodList = new LinkedList<>(), evilList = new LinkedList<>(), neutralEvilList = new LinkedList<>(), neutralList = new LinkedList<>();
		for(LobbyPlayer lp : lobbyEntity.getAlivePlayers()) {
			switch(lp.getRole().getAlignment()) {
			case Good:
			case ChaoticGood:
				goodList.add(lp);
				break;
			case Evil:
			case ChaoticEvil:
				evilList.add(lp);
				break;
			case NeutralEvil:
				neutralEvilList.add(lp);
				break;
			case Neutral:
				neutralList.add(lp);
				break;
			}
		}
		
		if(!goodList.isEmpty() && evilList.isEmpty() && neutralEvilList.isEmpty()) {
			goodWins(goodList, evilList, neutralList, neutralEvilList, lobbyEntity);
			return true;
		} else if (!evilList.isEmpty() && goodList.isEmpty() && neutralEvilList.isEmpty()) {
			evilWins(goodList, evilList, neutralList, neutralEvilList, lobbyEntity);
			return true;
		} else if (goodList.isEmpty() && evilList.isEmpty()){
			neutralWins(goodList, evilList, neutralList, neutralEvilList, lobbyEntity);
			return true;
		} else 
			return false;
	}
	
	private void neutralWins(LinkedList<LobbyPlayer> goodPlayers, LinkedList<LobbyPlayer> evilPlayers, LinkedList<LobbyPlayer> neutralPlayers, LinkedList<LobbyPlayer> neutralEvilPlayers, LobbyEntity lobbyEntity) {
		goodPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
		evilPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
		neutralPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("won")})))));
		neutralEvilPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("won")})))));
		
		lobbyEntity.getDeadPlayers().forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
		broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("endgame")}))));
	
		System.out.println("Game has ended with a neutral win!");
	}
	
	private void goodWins(LinkedList<LobbyPlayer> goodPlayers, LinkedList<LobbyPlayer> evilPlayers, LinkedList<LobbyPlayer> neutralPlayers, LinkedList<LobbyPlayer> neutralEvilPlayers, LobbyEntity lobbyEntity) {
		goodPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("won")})))));
		evilPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
		neutralPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("won")})))));
		neutralEvilPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
	
		lobbyEntity.getDeadPlayers().forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
		broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("endgame")}))));
	
		System.out.println("Game has ended with a good win!");
	}
	
	private void evilWins(LinkedList<LobbyPlayer> goodPlayers, LinkedList<LobbyPlayer> evilPlayers, LinkedList<LobbyPlayer> neutralPlayers, LinkedList<LobbyPlayer> neutralEvilPlayers, LobbyEntity lobbyEntity) {
		goodPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
		evilPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("won")})))));
		neutralPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("won")})))));
		neutralEvilPlayers.forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
	
		lobbyEntity.getDeadPlayers().forEach((p) -> privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("lost")})))));
		broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("endgame")}))));
	
		System.out.println("Game has ended with an evil win!");
	}
	
	private void startWaitingPhase(LobbyEntity lobbyEntity, String nextPhase) {
		new Thread() {
			public void run() {
				try {
					if(lobbyEntity.getPhaseTime() > 0) {
						Thread.sleep(1000);
						lobbyEntity.setPhaseTime(lobbyEntity.getPhaseTime() - 1);
						run();
					} else {
						runNextPhase(lobbyEntity, nextPhase);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private void runNextPhase(LobbyEntity lobbyEntity, String nextPhase) {
		switch(nextPhase) {
		case "nightphase":
			nightPhase(lobbyEntity);
			break;
		case "dayphase":
			dayPhase(lobbyEntity);
			break;
		}
	}
	
	private void startNight(LobbyEntity lobbyEntity) {
		lobbyEntity.setPhaseTime(30);
		new Thread() {
			public void run() {
				try {
					if(lobbyEntity.getPhaseTime() > 0) {
						Thread.sleep(1000);
						lobbyEntity.setPhaseTime(lobbyEntity.getPhaseTime() - 1);
						run();
					} else {
						endNight(lobbyEntity);
					}
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private void endNight(LobbyEntity lobbyEntity) {
		GameEmulator game = new GameEmulator(lobbyEntity.getRounds());
		
		Map<String, EmulationCharacter> emulationCharacters = setUpCharacters(lobbyEntity);
		
		for(EmulationCharacter emulationChar : emulationCharacters.values()) {
			emulationChar.getRole().doAction(game, emulationChar, emulationCharacters.get(emulationChar.getTargetid()));
		}
		
		game.emulate();
		
		for(EmulationCharacter emulationChar : emulationCharacters.values()) {
			List<LobbyMessage> messageList = new ArrayList<>();
			
			messageList.addAll(emulationChar.getMessageList());
			
			privateMessage(emulationChar.getLobbyPlayer().getUser().getUsername(), JoinLobbyService.convertObjectToJson(messageList));
		}
		
		game.getDeadPlayers().forEach((p) -> { broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("kill", p.getLobbyPlayer().getId(), p.getLobbyPlayer().getNickname(), p.getLobbyPlayer().getRole().getName(), p.getLobbyPlayer().getAlignment())})))); lobbyEntity.addDeadPlayer(p.getLobbyPlayer()); } );
		
		waitPhase(lobbyEntity, "dayphase");
		
		// Setting all targets to null and telling clients about the change
		lobbyEntity.getAlivePlayers().forEach((p) -> { if(p.getTarget() != null) { privateMessage(p.getUser().getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("unnightaction", p.getTarget())})))); p.setTarget(null);}});
	
		lobbyEntity.setRounds(lobbyEntity.getRounds() + 1);
	}
	
	private void startDay(LobbyEntity lobbyEntity) {
		lobbyEntity.setPhaseTime(60);
		new Thread() {
			public void run() {
				try {
					if(lobbyEntity.getPhaseTime() > 0) {
						Thread.sleep(1000);
						lobbyEntity.setPhaseTime(lobbyEntity.getPhaseTime() - 1);
						run();
					} else {
						endDay(lobbyEntity);
					}
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private void endDay(LobbyEntity lobbyEntity) {
		waitPhase(lobbyEntity, "nightphase");
		
		// Setting all votes to 0 and telling clients about the change
		lobbyEntity.getAlivePlayers().forEach((p) -> { if(p.getVotes() != 0) p.setVotes(0); if(p.getVoted() != null) { broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(new ArrayList<>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("updatevotestatus", p.getId(), p.getVoted(), "0", "x")})))); p.setVoted(null);}});
	}
	
	private Map<String, EmulationCharacter> setUpCharacters(LobbyEntity lobbyEntity) {
		Map<String, EmulationCharacter> emulationCharacters = new HashMap<>();
		
		lobbyEntity.getAlivePlayers().forEach((p) -> emulationCharacters.put(p.getId(), new EmulationCharacter(p, p.getRole(), p.getTarget())));
		
		return emulationCharacters;
	}
}
