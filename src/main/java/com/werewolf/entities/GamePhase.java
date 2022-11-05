package com.werewolf.entities;

public enum GamePhase {
    LOBBY("lobby"), DAY("dayphase"), WAIT("waitphase"), NIGHT("nightphase");

    private final String name;

    GamePhase(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}
