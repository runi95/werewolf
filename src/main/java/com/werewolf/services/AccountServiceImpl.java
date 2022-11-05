package com.werewolf.services;

import com.werewolf.data.*;
import com.werewolf.entities.User;
import com.werewolf.entities.UserStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRightRepository userRightRepository;

    @Autowired
    UserStatisticsRepository userStatisticsRepository;

    @Override
    public void create(AccountRegisterForm accountRegisterForm) {
        User user = new User();
        user.setUsername(accountRegisterForm.getUsername());
        user.setPasswordHash(new BCryptPasswordEncoder().encode(accountRegisterForm.getPassword()));
        user.setRights(new HashSet<>(userRightRepository.findAll()));

        UserStatistics userStatistics = new UserStatistics();
        userStatistics.setUsername(user.getUsername());
        userStatistics.setGamesplayed(0);
        userStatistics.setGameswon(0);

        userStatisticsRepository.save(userStatistics);
        accountRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("A user with that username does not exist"));
    }

    @Override
    @Transactional
    public User findById(long id) {
        Optional<User> user = accountRepository.findById(id);
        if (user.isEmpty())
            throw new IllegalArgumentException("A user with that id does not exist");

        return user.get();
    }

    @Override
    public void update(User user, UserEditForm userEditForm) {
        user.setUsername(userEditForm.getUsername());
        accountRepository.save(user);
    }

    @Override
    public UserEditForm getEditForm(User user) {
        UserEditForm userEditForm = new UserEditForm();
        userEditForm.setUsername(user.getUsername());
        return userEditForm;
    }
}
