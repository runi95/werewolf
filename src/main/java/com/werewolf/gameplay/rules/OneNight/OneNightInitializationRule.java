package com.werewolf.gameplay.rules.OneNight;

import com.werewolf.Messages.PlayerMessage;
import com.werewolf.Messages.PlayerMessageType;
import com.werewolf.entities.GamePhase;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.gameplay.Roles;
import com.werewolf.gameplay.rules.LobbyInitializationRule;
import com.werewolf.gameplay.rules.PlayerInitializationRule;

import java.util.*;

public class OneNightInitializationRule implements LobbyInitializationRule, PlayerInitializationRule {

    public List<PlayerMessage> initializeLobby(LobbyEntity lobbyEntity) {
        lobbyEntity.setStartedState(true);
        List<PlayerMessage> messages = new ArrayList<>();
        for(LobbyPlayer p : lobbyEntity.getPlayers()) {
            Roles role = Roles.Werewolf_OneNight;
            p.setRole(role);
            p.setAlignment(role.getAlignment().getAlignmentName());
        }

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("action", "loadgame");
        messages.add(new PlayerMessage(PlayerMessageType.BROADCAST, lobbyEntity.getGameId(), messageMap));

        startWaitPhase(lobbyEntity);

        return messages;
    }

    private void startWaitPhase(LobbyEntity lobbyEntity) {
        Runnable task = () -> {
            try {Thread.sleep(2000);}
            catch (InterruptedException e) {e.printStackTrace();}
            lobbyEntity.setPhase(GamePhase.NIGHT);
        };
        new Thread(task).start();
    }

    public List<PlayerMessage> initializePlayer(LobbyEntity lobbyEntity, LobbyPlayer lobbyPlayer) {
        List<PlayerMessage> messageList = new ArrayList<>();
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("action", "role");
        roleMap.put("name", lobbyPlayer.getRole().getName());
        roleMap.put("align", lobbyPlayer.getRole().getAlignment().getAlignmentName());
        roleMap.put("goal", lobbyPlayer.getRole().getAlignment().getGoal());
        roleMap.put("desc", lobbyPlayer.getRole().getDescription());

        messageList.add(new PlayerMessage(PlayerMessageType.PRIVATE, lobbyPlayer.getUser().getUsername(), roleMap));

        Map<String, Object> alivePlayersMap = new HashMap<>();
        alivePlayersMap.put("action", "addalive");
        List<Map<String, Object>> alivePlayersList = new ArrayList<>();
        for(LobbyPlayer p : lobbyEntity.getAlivePlayers()) {
            Map<String, Object> alivePlayer = new HashMap<>();
            alivePlayer.put("playerid", p.getId());
            alivePlayer.put("name", p.getNickname());
            alivePlayer.put("votes", p.getVotes());
            alivePlayersList.add(alivePlayer);
        }
        alivePlayersMap.put("players", alivePlayersList);
        messageList.add(new PlayerMessage(PlayerMessageType.PRIVATE, lobbyPlayer.getUser().getUsername(), alivePlayersMap));

        Map<String, Object> deadPlayersMap = new HashMap<>();
        deadPlayersMap.put("action", "adddead");
        List<Map<String, Object>> deadPlayersList = new ArrayList<>();
        for(LobbyPlayer p : lobbyEntity.getDeadPlayers()) {
            Map<String, Object> deadPlayer = new HashMap<>();
            deadPlayer.put("playerid", p.getId());
            deadPlayer.put("name", p.getNickname());
            deadPlayer.put("role", p.getRole().getName());
            deadPlayer.put("align", p.getAlignment());
            deadPlayersList.add(deadPlayer);
        }
        deadPlayersMap.put("players", deadPlayersList);
        messageList.add(new PlayerMessage(PlayerMessageType.PRIVATE, lobbyPlayer.getUser().getUsername(), deadPlayersMap));

        return messageList;
    }

}
