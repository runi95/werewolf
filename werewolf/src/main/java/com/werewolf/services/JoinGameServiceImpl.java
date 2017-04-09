package com.werewolf.services;

import com.werewolf.data.GameEntityRepository;
import com.werewolf.data.JoinGameForm;
import com.werewolf.data.LobbyEntityRepository;
import com.werewolf.entities.GameEntity;
import com.werewolf.entities.LobbyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Repository
public class JoinGameServiceImpl implements JoinGameService {

    @Autowired
    GameEntityRepository gameEntityRepository;

    // Assumes that there are no games with given id
    @Override
    public void create(JoinGameForm joinGameForm) {
        GameEntity gameEntity = new GameEntity();
    }

    @Override
    public GameEntity findByGameId(String gameId) {
        return gameEntityRepository.findByGameid(gameId).orElseThrow(() -> new IllegalArgumentException("A game with that gameId does not exist"));
    }

    @Override
    @Transactional
    public GameEntity findById(long id) {
        GameEntity game = gameEntityRepository.findOne(id);
        if(game != null)
            return game;
        else {
            throw new IllegalArgumentException("A lobby with that id does not exist");
        }
    }

    @Override
    public JoinGameForm getEditForm(GameEntity gameEntity) {
        JoinGameForm joinGameForm = new JoinGameForm();
        joinGameForm.setGameId(gameEntity.getGame_id());
        return joinGameForm;
    }
}
