package com.golfcharity.repository;

import com.golfcharity.entity.GolfScore;
import com.golfcharity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GolfScoreRepository extends JpaRepository<GolfScore, Long> {
    List<GolfScore> findByUserOrderByScoreDateDesc(User user);
}
