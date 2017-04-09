package com.werewolf.services;

import com.werewolf.data.JoinGameForm;
import com.werewolf.data.LobbyEntityRepository;
import com.werewolf.entities.LobbyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Repository
public class JoinGameServiceImpl implements JoinGameService {

    @Autowired
    LobbyEntityRepository lobbyEntityRepository;

    // Assumes that there are no games with given id
    @Override
    public void create(JoinGameForm joinGameForm) {
        LobbyEntity lobbyEntity = new LobbyEntity();
        lobbyEntityRepository.saveAndFlush(lobbyEntity);
    }

    @Override
    public LobbyEntity findByGameId(String gameId) {
        return lobbyEntityRepository.findByGameId(gameId).orElseThrow(() -> new IllegalArgumentException("A game with that gameId does not exist"));
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
    public JoinGameForm getEditForm(LobbyEntity lobbyEntity) {
        JoinGameForm joinGameForm = new JoinGameForm();
        joinGameForm.setGameId(lobbyEntity.getGame_id());
        return joinGameForm;
    }
}
