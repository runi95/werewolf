package com.werewolf.services;


import com.werewolf.data.JoinLobbyForm;
import com.werewolf.data.LobbyEntityRepository;
import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
public class JoinLobbyServiceImpl implements JoinLobbyService {

    @Autowired
    LobbyEntityRepository lobbyEntityRepository;

    // Assumes that there are no games with given id
    @Override
    public LobbyEntity create(JoinLobbyForm joinLobbyForm) {
        LobbyEntity lobbyEntity = new LobbyEntity();
        LobbyPlayer lobbyPlayer = new LobbyPlayer();

        lobbyPlayer.setNickname(joinLobbyForm.getNickname());
        lobbyPlayer.setUser(joinLobbyForm.getUser());

        String gameid = generateNewGameid();
        lobbyEntity.setGameid(gameid);
        lobbyEntity.getPlayers().add(lobbyPlayer);

//        System.out.println("Lobby id: " + lobbyEntity.getId());
//        System.out.println("Game id: " + lobbyEntity.getGameId());
        lobbyEntityRepository.saveAndFlush(lobbyEntity);
        return lobbyEntity;
    }

    @Override
    public LobbyEntity findByGameId(String gameId) {
        return lobbyEntityRepository.findByGameid(gameId).orElseThrow(() -> new IllegalArgumentException("A lobby with that gameId does not exist"));
    }

    @Override
    @Transactional
    public LobbyEntity findById(long id) {
        LobbyEntity lobby = lobbyEntityRepository.findOne(id);
        if(lobby != null)
            return lobby;
        else {
            throw new IllegalArgumentException("A lobby with that id does not exist");
        }
    }

    @Override
    public JoinLobbyForm getEditForm(LobbyEntity lobbyEntity) {
        JoinLobbyForm joinLobbyForm = new JoinLobbyForm();
        joinLobbyForm.setGameid(lobbyEntity.getGameId());
        return joinLobbyForm;
    }

    @Override
    public boolean gameidIsPresent(String gameid) {
        return lobbyEntityRepository.findByGameid(gameid).isPresent();
    }

    // TODO: Possibly make this failsafe
    private String generateNewGameid() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(58786560) + 1679616;
        String generatedGameid = Integer.toUnsignedString(num, 36).toUpperCase();

        if(gameidIsPresent(generatedGameid))
            return generateNewGameid();
        else return generatedGameid;
    }
}
