package com.werewolf.gameplay.rules.OneNight;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.gameplay.rules.VoteRule;

import java.util.List;

public class VoteRuleEndsGame implements VoteRule {

    public List<PlayerMessage> vote(LobbyEntity lobbyEntity, LobbyPlayer voter, LobbyPlayer target, LobbyPlayer oldTarget, boolean flag) {
        // First check that both the voter and target is alive and that the vote is valid.

        if(lobbyEntity.getAlivePlayer(voter.getId()) == null)
            return null;

        if(lobbyEntity.getAlivePlayer(target.getId()) == null)
            return null;

        // Now check that the new target isn't the same as the old target.
        if(target.getId().equals(oldTarget.getId()))
            return null;

        voter.setVoted(target.getId());
        oldTarget.setVotes(oldTarget.getVotes() - 1);
        target.setVotes(target.getVotes() + 1);

        // Check if the target has enough votes to be lynched.
        if(target.getVotes() >= (lobbyEntity.getAliveCount() / 2) + 1) {
            // Our target is definitely getting lynched.

        } else {

        }

        return null;
    }

}
