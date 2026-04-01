package com.golfcharity.repository;

import com.golfcharity.entity.CharityContribution;
import com.golfcharity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface CharityContributionRepository extends JpaRepository<CharityContribution, Long> {
    Optional<CharityContribution> findByUser(User user);
    List<CharityContribution> findByCharityId(Long charityId);
    void deleteByCharityId(Long charityId);
}
