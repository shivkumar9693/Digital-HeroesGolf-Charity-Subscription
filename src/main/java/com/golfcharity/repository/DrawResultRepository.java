package com.golfcharity.repository;

import com.golfcharity.entity.Draw;
import com.golfcharity.entity.DrawResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DrawResultRepository extends JpaRepository<DrawResult, Long> {
    List<DrawResult> findByDraw(Draw draw);
}
