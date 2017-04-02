package com.werewolf.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.werewolf.entities.UserRight;

public interface UserRightRepository extends JpaRepository<UserRight, Long> {
}

