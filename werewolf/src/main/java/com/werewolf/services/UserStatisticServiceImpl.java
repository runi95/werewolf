package com.werewolf.services;

import com.werewolf.data.AccountRepository;
import com.werewolf.data.UserStatisticsRepository;
import com.werewolf.entities.User;
import com.werewolf.entities.UserStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStatisticServiceImpl implements UserStatisticService {

    @Autowired
    UserStatisticsRepository userStatisticsRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserStatistics getUserStatistics(String username) {
        if(!userStatisticsRepository.exists(username) && accountRepository.findByUsername(username).isPresent()) {
            User user = accountRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException());

            UserStatistics userStatistics = new UserStatistics();
            userStatistics.setUsername(user.getUsername());
            userStatistics.setGamesplayed(0);
            userStatistics.setGameswon(0);

            userStatisticsRepository.save(userStatistics);
        }

        return userStatisticsRepository.findOne(username);
    }

    @Override
    public long getGamesplayed(String username) {
        UserStatistics userStatistics = getUserStatistics(username);
        return userStatistics.getGamesplayed();
    }

    @Override
    public long getGameswon(String username) {
        UserStatistics userStatistics = getUserStatistics(username);
        return userStatistics.getGamesplayed();
    }
}
