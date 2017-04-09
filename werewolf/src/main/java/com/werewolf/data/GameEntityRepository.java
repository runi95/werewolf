package com.werewolf.data;

import java.util.Optional;
import com.werewolf.entities.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface GameEntityRepository extends JpaRepository<GameEntity, Long> {
    Optional<GameEntity> findByGameid(String gameid);
}
