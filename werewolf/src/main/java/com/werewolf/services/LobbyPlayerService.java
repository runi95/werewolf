package com.werewolf.services;

import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.entities.User;

import java.util.List;

public interface LobbyPlayerService {
    LobbyPlayer findByUser(User user);
    List<LobbyPlayer> findAllByLobby(LobbyEntity lobby);

}
