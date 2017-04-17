package com.werewolf.data;

import com.werewolf.entities.LobbyEntity;
import com.werewolf.entities.LobbyPlayer;
import com.werewolf.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public interface LobbyPlayerRepository extends JpaRepository<LobbyPlayer, Long> {
    Optional<LobbyPlayer> findByUser(User user);
    List<LobbyPlayer> findAllByLobby(LobbyEntity lobby);
}
