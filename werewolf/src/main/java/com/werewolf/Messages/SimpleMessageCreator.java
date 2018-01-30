package com.werewolf.Messages;

import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SimpleMessageCreator {

    ArrayList<PlayerMessage> messageList = new ArrayList<>();

    /**
     * MessageType = BROADCAST
     * Informs everyone that the specified player has died.
     */
    public void kill(LobbyEntity lobbyEntity, LobbyPlayer dead) {
        Map<String, Object> map = new HashMap<>();
        map.put("action", "killed");
        map.put("player", dead.getId());
        map.put("role", dead.getRole().getName());
        map.put("alignment", dead.getRole().getAlignment());

        PlayerMessage playerMessage = new PlayerMessage();
        playerMessage.setPlayerMessageType(PlayerMessageType.BROADCAST);
        playerMessage.setReceiverId(lobbyEntity.getGameId());
        playerMessage.setMessageMap(map);

        messageList.add(playerMessage);
    }

    /**
     * MessageType = BROADCAST
     * Sends out a chat message from the specified player.
     */
    public void chat(LobbyEntity lobbyEntity, LobbyPlayer sender, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("action", "print");

        String txt = sender.getNickname() + ": " + message;
        map.put("str", txt);

        PlayerMessage playerMessage = new PlayerMessage();
        playerMessage.setPlayerMessageType(PlayerMessageType.BROADCAST);
        playerMessage.setReceiverId(lobbyEntity.getGameId());
        playerMessage.setMessageMap(map);

        messageList.add(playerMessage);
    }

    /**
     * MessageType = PRIVATE
     * Prints a message for the specified player.
     * This can be used to send information to a specific player.
     */
    public void print(LobbyPlayer receiver, String txt) {
        Map<String, Object> map = new HashMap<>();
        map.put("action", "print");
        map.put("str", txt);

        PlayerMessage playerMessage = new PlayerMessage();
        playerMessage.setPlayerMessageType(PlayerMessageType.PRIVATE);
        playerMessage.setReceiverId(receiver.getUser().getUsername());
        playerMessage.setMessageMap(map);

        messageList.add(playerMessage);
    }

    public ArrayList<PlayerMessage> getMessageList() {
        return messageList;
    }

}
