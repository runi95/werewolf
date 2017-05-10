package com.werewolf.gameplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.werewolf.Messages.LobbyMessage;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.services.JoinLobbyService;

@Service
public class AdvancedMode implements GameMode {
	
	@Autowired
	SimpMessagingTemplate simpTemplate;
	
	@Override
	public void initalizeGame(LobbyEntity lobbyEntity) {
		if(lobbyEntity.getStartedState())
			return;
		else {
			lobbyEntity.setStartedState(true);
			waitPhase(lobbyEntity, "nightphase");
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
		
		if(oldVoteTarget != null) {
			oldVoteTarget.setVotes(oldVoteTarget.getVotes() - 1);
			messageList.add(new LobbyMessage("updatevotestatus", voter.getId(), oldVoteTarget.getId(), Integer.toString(oldVoteTarget.getVotes()), "-"));
		}
		
    	if(status) {
    		voteTarget.setVotes(voteTarget.getVotes() + 1);
    		voter.setVoted(voteTarget.getId());
    	
    		checkVotes(lobbyEntity, voteTarget);
    		
    		messageList.add(new LobbyMessage("updatevotestatus", voter.getId(), voteTarget.getId(), Integer.toString(voteTarget.getVotes()), "+"));
    	} else {
    		voteTarget.setVotes(voteTarget.getVotes() - 1);
    		voter.setVoted(null);
    		
    		messageList.add(new LobbyMessage("updatevotestatus", voter.getId(), voteTarget.getId(), Integer.toString(voteTarget.getVotes()), "-"));
    	}
    	
    	if(!messageList.isEmpty())
			broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));
	}
	
	private void checkVotes(LobbyEntity lobbyEntity, LobbyPlayer latestVotedOn) {
		List<LobbyMessage> messageList = new ArrayList<>();
		LobbyEntity lobby = latestVotedOn.getLobby();

		if (latestVotedOn.getVotes() >= (Math.ceil(lobby.getAliveCount() / 2.0))) {
			lobby.addDeadPlayer(latestVotedOn);

			messageList.add(new LobbyMessage("lynch", latestVotedOn.getId(), latestVotedOn.getNickname(),
					latestVotedOn.getRole().getName(), latestVotedOn.getAlignment()));
			
			waitPhase(lobbyEntity, "nightphase");
		}
		
		if(!messageList.isEmpty())
			broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));
	}
	
	public void dayPhase(LobbyEntity lobbyEntity) {
		System.out.println("Initialized day phase!");
		List<LobbyMessage> messageList = new ArrayList<>();
		lobbyEntity.setPhase("dayphase");
		
		messageList.add(new LobbyMessage("dayphase"));
		
		if(!messageList.isEmpty())
			broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));
	}
	
	public void nightPhase(LobbyEntity lobbyEntity) {
		System.out.println("Initialized night phase!");
		List<LobbyMessage> messageList = new ArrayList<>();
		lobbyEntity.setPhase("nightphase");
		
		messageList.add(new LobbyMessage("nightphase"));
		
		if(!messageList.isEmpty())
			broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));
		
		startNight(lobbyEntity);
	}
	
	private void waitPhase(LobbyEntity lobbyEntity, String nextPhase) {
		System.out.println("Waiting for... " + nextPhase);
		List<LobbyMessage> messageList = new ArrayList<>();
		
		lobbyEntity.setPhase("waitphase");
		lobbyEntity.setPhaseTime(3);
		
		messageList.add(new LobbyMessage("waitphase"));
		
		if(!messageList.isEmpty())
			broadcastMessage(lobbyEntity.getGameId(), JoinLobbyService.convertObjectToJson(messageList));
		
		startWaitingPhase(lobbyEntity, nextPhase);
	}
	
	private void startWaitingPhase(LobbyEntity lobbyEntity, String nextPhase) {
		new Thread() {
			public void run() {
				try {
					if(lobbyEntity.getPhaseTime() > 0) {
						System.out.println("waitphase sleeps left: " + lobbyEntity.getPhaseTime());
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
		lobbyEntity.setPhaseTime(5);
		new Thread() {
			public void run() {
				try {
					if(lobbyEntity.getPhaseTime() > 0) {
						System.out.println("nightphase sleeps left: " + lobbyEntity.getPhaseTime());
						Thread.sleep(1000);
						lobbyEntity.setPhaseTime(lobbyEntity.getPhaseTime() - 1);
						run();
					} else {
						endNight(lobbyEntity);
						dayPhase(lobbyEntity);
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
			
			emulationChar.getMessageList().forEach((msg) -> messageList.add(new LobbyMessage("nightmessage", msg)));
			
			privateMessage(emulationChar.getUsername(), JoinLobbyService.convertObjectToJson(messageList));
		}
		
		game.getDeadPlayers().forEach((p) -> privateMessage(p.getUsername(), JoinLobbyService.convertObjectToJson(new ArrayList<LobbyMessage>(Arrays.asList(new LobbyMessage[]{new LobbyMessage("dead")})))));
		
		startWaitingPhase(lobbyEntity, "dayphase");
	}
	
	private Map<String, EmulationCharacter> setUpCharacters(LobbyEntity lobbyEntity) {
		Map<String, EmulationCharacter> emulationCharacters = new HashMap<>();
		
		lobbyEntity.getAlivePlayers().forEach((p) -> emulationCharacters.put(p.getId(), new EmulationCharacter(p.getUser().getUsername(), p.getId(), p.getRole(), p.getTarget())));
		
		return emulationCharacters;
	}
	
	private void broadcastMessage(String gameid, String message) {
		simpTemplate.convertAndSend("/action/broadcast/" + gameid, message);
	}
	
	private void privateMessage(String user, String message) {
		simpTemplate.convertAndSendToUser(user, "/action/private", message);
	}
}
