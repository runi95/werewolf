package com.werewolf.data;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.werewolf.entities.User;

public interface AccountRepository extends CrudRepository<User, Long> {
	Optional<User> findByUsername(String username);
}
