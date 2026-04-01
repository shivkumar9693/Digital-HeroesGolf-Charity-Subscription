package com.golfcharity.repository;

import com.golfcharity.entity.Draw;
import com.golfcharity.entity.DrawEntry;
import com.golfcharity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DrawEntryRepository extends JpaRepository<DrawEntry, Long> {
    List<DrawEntry> findByDraw(Draw draw);
    List<DrawEntry> findByUser(User user);
    Optional<DrawEntry> findByUserAndDraw(User user, Draw draw);
}
