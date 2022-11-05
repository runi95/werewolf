package com.werewolf.gameplay.rules.OneNight;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.Messages.PlayerMessageType;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.gameplay.rules.DayRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayRuleEveryoneCanVoteOnAlivePlayersExceptSelf implements DayRule{

    public List<PlayerMessage> dayStarted(LobbyEntity lobbyEntity) {
        List<PlayerMessage> messageList = new ArrayList<>();

        for(LobbyPlayer p : lobbyEntity.getPlayers()) {
            Map<String, Object> playerMessageMap = new HashMap<>();
            playerMessageMap.put("action", "phasechange");
            playerMessageMap.put("phase", "day");
            playerMessageMap.put("type", "blacklist");
            playerMessageMap.put("exp", new String[] {"self"});
            messageList.add(new PlayerMessage(PlayerMessageType.PRIVATE, p.getUser().getUsername(), playerMessageMap));
        }

        return messageList;
    }
}
