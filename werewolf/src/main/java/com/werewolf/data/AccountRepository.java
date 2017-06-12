package com.werewolf.data;

import com.werewolf.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<User, Long> {
	Optional<User> findByUsername(String username);
}
