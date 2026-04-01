package com.golfcharity.service;

import com.golfcharity.dto.ScoreEntryDto;
import com.golfcharity.entity.GolfScore;
import com.golfcharity.entity.User;
import com.golfcharity.repository.GolfScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService {
    
    private final GolfScoreRepository scoreRepository;
    
    @Transactional
    public void addScore(User user, ScoreEntryDto dto) {
        List<GolfScore> existingScores = scoreRepository.findByUserOrderByScoreDateDesc(user);
        
        GolfScore newScore = GolfScore.builder()
                .user(user)
                .score(dto.getScore())
                .scoreDate(dto.getScoreDate())
                .build();
                
        scoreRepository.save(newScore);
        
        // Rolling logic: if total scores > 5, delete oldest
        if (existingScores.size() >= 5) {
            existingScores.add(newScore);
            existingScores.sort(Comparator.comparing(GolfScore::getScoreDate).reversed());
            
            // Delete scores beyond the top 5 newest
            List<GolfScore> toDelete = existingScores.subList(5, existingScores.size());
            scoreRepository.deleteAll(toDelete);
        }
    }
    
    public List<GolfScore> getUserScores(User user) {
        return scoreRepository.findByUserOrderByScoreDateDesc(user);
    }
    
    @Transactional
    public void deleteScore(Long id, User user) {
        GolfScore score = scoreRepository.findById(id).orElseThrow();
        if (score.getUser().getId().equals(user.getId())) {
            scoreRepository.delete(score);
        }
    }
}
