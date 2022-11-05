package com.werewolf.data;

import com.werewolf.entities.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, String> {
}

