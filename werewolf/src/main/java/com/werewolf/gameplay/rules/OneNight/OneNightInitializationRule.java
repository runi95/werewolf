package com.werewolf.gameplay.rules.OneNight;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.Messages.PlayerMessageType;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.gameplay.Roles;
import com.werewolf.gameplay.rules.InitializationRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneNightInitializationRule implements InitializationRule {

    public List<PlayerMessage> initialize(LobbyEntity lobbyEntity) {
        System.out.println("OneNightInitializationRule is initializing");
        lobbyEntity.setStartedState(true);
        List<PlayerMessage> messages = new ArrayList<>();
        for(LobbyPlayer p : lobbyEntity.getPlayers()) {
            System.out.println("Giving " + p.getNickname() + " a role");
            Map<String, Object> roleMap = new HashMap<>();
            roleMap.put("action", "role");
            Roles role = Roles.Bandit;
            roleMap.put("rolename", role.getName());
            roleMap.put("rolealign", role.getAlignment());
            roleMap.put("roledesc", role.getDescription());
            PlayerMessage roleMessage = new PlayerMessage();
            roleMessage.setPlayerMessageType(PlayerMessageType.PRIVATE);
            roleMessage.setReceiverId(p.getUser().getUsername());
            roleMessage.setMessageMap(roleMap);
            messages.add(roleMessage);
        }

        return messages;
    }

}
