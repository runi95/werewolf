package com.werewolf.gameplay.rules.OneNight;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.Messages.PlayerMessageType;
import com.werewolf.entities.GamePhase;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.gameplay.rules.ChatRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRuleCanOnlyChatDuringDayAndLobbyPhase implements ChatRule{

    public List<PlayerMessage> chat(LobbyEntity lobbyEntity, LobbyPlayer chatSourcePlayer, String message) {
        List<PlayerMessage> playerMessages = new ArrayList<>();

        if((lobbyEntity.getAlivePlayer(chatSourcePlayer.getId()) != null && lobbyEntity.getPhase() == GamePhase.DAY)|| lobbyEntity.getPhase() == GamePhase.LOBBY) {
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("action", "print");
            messageMap.put("message", "|c200155000" + chatSourcePlayer.getNickname() + "|: " + message);
            playerMessages.add(new PlayerMessage(PlayerMessageType.BROADCAST, lobbyEntity.getGameId(), messageMap));
        }

        return playerMessages;
    }

}
