package com.werewolf.services;

import com.werewolf.entities.UserStatistics;

public interface UserStatisticService {

    UserStatistics getUserStatistics(String username);

    long getGamesplayed(String username);
    long getGameswon(String username);

}
