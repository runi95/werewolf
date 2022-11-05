package com.werewolf.Messages;

public enum PlayerMessageType {

    /**
     * Indicates that a message should be sent to one specific person alone.
     */
    PRIVATE,

    /**
     * Indicates that a message should be broadcast to every player in the lobby indifferent to the status of the player.
     */
    BROADCAST
}
