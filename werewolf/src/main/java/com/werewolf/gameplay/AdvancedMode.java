package com.werewolf.gameplay;

import com.werewolf.Messages.LobbyMessage;
import com.werewolf.entities.GamePhase;
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
	public List<RoleInterface> setRoles(LobbyEntity lobbyEntity) {
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
			lottery.add(Roles.Mermaid);
		case 1:
			lottery.add(Roles.Marauder);
		}

		return lottery;
	}

	@Override
	public void vote(LobbyEntity lobbyEntity, LobbyPlayer voter, LobbyPlayer voteTarget, LobbyPlayer oldVoteTarget, boolean status) {
		if(lobbyEntity.getPhase() != GamePhase.DAY)
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
}
