package com.golfcharity.controller;

import com.golfcharity.dto.ScoreEntryDto;
import com.golfcharity.entity.User;
import com.golfcharity.repository.UserRepository;
import com.golfcharity.service.ScoreService;
import com.golfcharity.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ScoreService scoreService;
    private final SubscriptionService subscriptionService;
    private final UserRepository userRepository;
    private final com.golfcharity.service.CharityService charityService;

    private User getAuthenticatedUser(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName()).orElseThrow();
    }

    @GetMapping
    public String dashboard(Model model, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        
        // Admin redirect logic
        if (user.getRole() == com.golfcharity.entity.Role.ADMIN) {
            return "redirect:/admin/dashboard";
        }

        // Sync role from DB with current session to avoid stale Role.PUBLIC_VISITOR status after payment
        if (authentication.getAuthorities().size() != user.getAuthorities().size()) {
            Authentication newAuth = new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
        
        model.addAttribute("user", user);
        model.addAttribute("scores", scoreService.getUserScores(user));
        model.addAttribute("subscription", subscriptionService.getUserSubscription(user));
        return "dashboard";
    }
    
    @GetMapping("/scores")
    public String viewScores(Model model, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        model.addAttribute("user", user);
        model.addAttribute("scores", scoreService.getUserScores(user));
        return "scores";
    }

    @PostMapping("/scores")
    public String addScore(@ModelAttribute ScoreEntryDto scoreDto, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        scoreService.addScore(user, scoreDto);
        return "redirect:/dashboard";
    }
    
    @GetMapping("/charity-selection")
    public String charitySelection(Model model, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        model.addAttribute("userCharity", charityService.getUserContribution(user));
        model.addAttribute("charities", charityService.getAllCharities());
        return "charity-selection";
    }
    
    @PostMapping("/charity-selection")
    public String saveCharitySelection(@ModelAttribute com.golfcharity.dto.CharitySelectionDto dto, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        charityService.selectCharity(user, dto);
        return "redirect:/dashboard/charity-selection?success";
    }
}
