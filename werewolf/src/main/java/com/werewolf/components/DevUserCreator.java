package com.werewolf.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.werewolf.data.AccountRepository;
import com.werewolf.entities.User;

@Component
public class DevUserCreator implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    AccountRepository userRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(!userRepository.findByUsername("admin").isPresent()) {
            User user = new User();
            user.setUsername("admin");
            user.setPasswordHash(new BCryptPasswordEncoder().encode("password"));
            userRepository.save(user);
        }
    }
}
