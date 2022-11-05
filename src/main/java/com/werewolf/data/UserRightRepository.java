package com.werewolf.data;

import com.werewolf.entities.UserRight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRightRepository extends JpaRepository<UserRight, Long> {
}

