package com.werewolf.services;


import com.werewolf.data.JoinLobbyForm;
import com.werewolf.data.LobbyEntityRepository;
import com.werewolf.entities.LobbyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JoinLobbyServiceImpl implements JoinLobbyService {

    @Autowired
    LobbyEntityRepository lobbyEntityRepository;

    // Assumes that there are no games with given id
    @Override
    public void create(JoinLobbyForm joinLobbyForm) {
        LobbyEntity lobbyEntity = new LobbyEntity();
        lobbyEntityRepository.saveAndFlush(lobbyEntity);
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
}
