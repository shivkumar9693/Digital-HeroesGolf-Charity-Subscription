package com.golfcharity.repository;

import com.golfcharity.entity.Charity;
import com.golfcharity.entity.CharityEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CharityEventRepository extends JpaRepository<CharityEvent, Long> {
    List<CharityEvent> findByCharity(Charity charity);
    void deleteByCharity(Charity charity);
}
