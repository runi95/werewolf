package com.werewolf.data;

import com.werewolf.entities.LobbyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface LobbyEntityRepository extends JpaRepository<LobbyEntity, Long> {
    Optional<LobbyEntity> findByGameid(String gameid);
}
