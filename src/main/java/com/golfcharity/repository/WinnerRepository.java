package com.golfcharity.repository;

import com.golfcharity.entity.User;
import com.golfcharity.entity.Winner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WinnerRepository extends JpaRepository<Winner, Long> {
    List<Winner> findByUser(User user);
}
