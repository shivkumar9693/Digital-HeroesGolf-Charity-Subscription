package com.golfcharity.repository;

import com.golfcharity.entity.Draw;
import com.golfcharity.entity.DrawStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DrawRepository extends JpaRepository<Draw, Long> {
    List<Draw> findByStatusOrderByPublishedAtDesc(DrawStatus status);
}
