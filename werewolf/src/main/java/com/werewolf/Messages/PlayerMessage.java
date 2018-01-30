package com.werewolf.Messages;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public class PlayerMessage {

    @JsonIgnore
    private PlayerMessageType playerMessageType;

    String receiverId;

    Map<String, Object> messageMap;

    public PlayerMessageType getPlayerMessageType() {
        return playerMessageType;
    }

    /**
     * Sets the message type to either BROADCAST or PRIVATE
     * a private message is only sent to the specified person
     * and a broadcast message is sent to every player in the lobby
     * @param playerMessageType
     */
    public void setPlayerMessageType(PlayerMessageType playerMessageType) {
        this.playerMessageType = playerMessageType;
    }

    public String getReceiverId() {
        return receiverId;
    }

    /**
     * Sets the receiver id for this message.
     * For broadcast messages this id should be equal to the game id the message is broadcast to, see LobbyEntity.
     * For private messages this id should be equal to the username of the receiver, see User.
     * @param receiverId
     */
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Map<String, Object> getMessageMap() {
        return messageMap;
    }

    /**
     * Sets the mapping of the message.
     * The map should contain all the required information needed to send this message.
     * @param messageMap
     */
    public void setMessageMap(Map<String, Object> messageMap) {
        this.messageMap = messageMap;
    }
}
