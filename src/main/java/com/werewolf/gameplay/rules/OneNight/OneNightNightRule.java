package com.werewolf.gameplay.rules.OneNight;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.Messages.PlayerMessageType;
import com.werewolf.entities.GamePhase;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.gameplay.rules.NightRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneNightNightRule implements NightRule {

    public List<PlayerMessage> nightStarted(LobbyEntity lobbyEntity) {
        List<PlayerMessage> messageList = new ArrayList<>();

        for(LobbyPlayer p : lobbyEntity.getPlayers()) {
            Map<String, Object> playerMessageMap = new HashMap<>();
            playerMessageMap.put("action", "phasechange");
            playerMessageMap.put("phase", "night");
            playerMessageMap.put("type", p.getNightExpressionType());
            playerMessageMap.put("exp", p.getNightexpression());
            messageList.add(new PlayerMessage(PlayerMessageType.PRIVATE, p.getUser().getUsername(), playerMessageMap));
        }

        Runnable task = () -> {
            try{ Thread.sleep(5000); }
            catch(InterruptedException e) { e.printStackTrace(); }
            lobbyEntity.setPhase(GamePhase.DAY);
        };
        new Thread(task).start();

        return messageList;
    }
}
