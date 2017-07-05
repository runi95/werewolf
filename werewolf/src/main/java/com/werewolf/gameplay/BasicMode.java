package com.werewolf.gameplay;

import com.werewolf.Messages.LobbyMessage;
import com.werewolf.entities.GamePhase;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.services.JoinLobbyService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BasicMode extends GameModeMasterClass {

    @Override
    public String getName() {
        return "Basic";
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
                lottery.add(Roles.Amnesiac);
            case 14:
                lottery.add(Roles.Knight);
            case 13:
                lottery.add(Roles.Bard);
            case 12:
                lottery.add(Roles.Bandit);
            case 11:
                lottery.add(Roles.Priest);
            case 10:
                lottery.add(Roles.Inquisitor);
            case 9:
                lottery.add(Roles.Bard);
            case 8:
                lottery.add(Roles.Bandit);
            case 7:
                lottery.add(Roles.Guard);
            case 6:
                lottery.add(Roles.Marauder);
                lottery.add(Roles.Bandit);
                lottery.add(Roles.Jester);
                lottery.add(Roles.Amnesiac);
                lottery.add(Roles.Inquisitor);
                lottery.add(Roles.Priest);
                break;
            case 5:
                lottery.add(Roles.Marauder);
                lottery.add(Roles.Priest);
                lottery.add(Roles.Amnesiac);
                lottery.add(Roles.Amnesiac);
                lottery.add(Roles.Bard);
                break;
            case 4:
                lottery.add(Roles.Marauder);
                lottery.add(Roles.Amnesiac);
                lottery.add(Roles.Priest);
                lottery.add(Roles.Bard);
                break;
            case 3:
                lottery.add(Roles.Bandit);
            case 2:
                lottery.add(Roles.Amnesiac);
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
