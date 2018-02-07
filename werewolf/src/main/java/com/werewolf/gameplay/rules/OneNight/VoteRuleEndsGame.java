package com.werewolf.gameplay.rules.OneNight;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.Messages.PlayerMessageType;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.gameplay.rules.VoteRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        List<PlayerMessage> messages = new ArrayList<>();

        Map<String, Object> messageMap = new HashMap<>();
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("playerid", voter.getId());
        messageArgs.put("votedon", target.getId());
        messageArgs.put("votes", Integer.toString(target.getVotes()));
        messageMap.put("action", "vote");
        messageMap.put("args", messageArgs);

        PlayerMessage playerMessage = new PlayerMessage();
        playerMessage.setPlayerMessageType(PlayerMessageType.BROADCAST);
        playerMessage.setReceiverId(lobbyEntity.getGameId());
        playerMessage.setMessageMap(messageMap);

        // Check if the target has enough votes to be lynched.
        if(target.getVotes() >= (lobbyEntity.getAliveCount() / 2) + 1) {
            // Our target is getting lynched.
            lobbyEntity.addDeadPlayer(target);

            Map<String, Object> lynchMap = new HashMap<>();
            Map<String, String> lynchArgs = new HashMap<>();
            lynchArgs.put("playerid", target.getId());
            lynchArgs.put("playername", target.getNickname());
            lynchArgs.put("playerrole", target.getRole().getName());
            lynchArgs.put("playeralignment", target.getRole().getAlignment().getAlignmentName());
            lynchMap.put("action", "killed");
            lynchMap.put("args", lynchArgs);

            PlayerMessage lynchMessage = new PlayerMessage();
            lynchMessage.setPlayerMessageType(PlayerMessageType.BROADCAST);
            lynchMessage.setReceiverId(lobbyEntity.getGameId());
            lynchMessage.setMessageMap(lynchMap);
            messages.add(lynchMessage);
        }

        return messages;
    }

}
