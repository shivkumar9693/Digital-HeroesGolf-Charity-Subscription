package com.golfcharity.repository;

import com.golfcharity.entity.Draw;
import com.golfcharity.entity.PrizePool;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PrizePoolRepository extends JpaRepository<PrizePool, Long> {
    Optional<PrizePool> findByDraw(Draw draw);
    Optional<PrizePool> findTopByOrderByIdDesc();
}
