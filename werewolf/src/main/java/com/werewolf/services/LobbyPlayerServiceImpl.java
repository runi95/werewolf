package com.werewolf.services;

import com.werewolf.data.LobbyPlayerRepository;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LobbyPlayerServiceImpl implements LobbyPlayerService {

    @Autowired
    LobbyPlayerRepository lobbyPlayerRepository;

    public LobbyPlayer findByUser(User user) {
        return lobbyPlayerRepository.findByUser(user).orElseThrow(() -> new IllegalArgumentException("A lobby with that user does not exist"));
    }

    public List<LobbyPlayer> findAllByLobby(LobbyEntity lobby) {
        return lobbyPlayerRepository.findAllByLobby(lobby);
    }

}
