package com.golfcharity.service;

import com.golfcharity.dto.CharitySelectionDto;
import com.golfcharity.entity.Charity;
import com.golfcharity.entity.CharityContribution;
import com.golfcharity.entity.User;
import com.golfcharity.repository.CharityContributionRepository;
import com.golfcharity.repository.CharityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharityService {

    private final CharityRepository charityRepository;
    private final CharityContributionRepository contributionRepository;

    public List<Charity> getAllCharities() {
        return charityRepository.findAll();
    }

    public Charity getCharityById(Long id) {
        return charityRepository.findById(id).orElseThrow();
    }

    public void selectCharity(User user, CharitySelectionDto dto) {
        Charity charity = charityRepository.findById(dto.getCharityId()).orElseThrow();
        CharityContribution contribution = contributionRepository.findByUser(user)
                .orElse(CharityContribution.builder().user(user).build());
        
        contribution.setCharity(charity);
        contribution.setPercentage(dto.getPercentage());
        contributionRepository.save(contribution);
    }
    
    public CharityContribution getUserContribution(User user) {
        return contributionRepository.findByUser(user).orElse(null);
    }
}
