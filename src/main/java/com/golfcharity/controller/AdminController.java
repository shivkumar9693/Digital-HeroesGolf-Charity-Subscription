package com.golfcharity.controller;

import com.golfcharity.entity.Charity;
import com.golfcharity.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final DrawRepository drawRepository;
    private final CharityRepository charityRepository;
    private final WinnerRepository winnerRepository;
    private final DrawResultRepository drawResultRepository;
    private final CharityContributionRepository contributionRepository;
    private final CharityEventRepository charityEventRepository;
    private final com.golfcharity.service.EmailService emailService;

    @GetMapping({"", "/", "/dashboard"})
    public String adminDashboard(Model model) {
        long totalUsers = userRepository.count();
        long totalDraws = drawRepository.count();
        long totalCharities = charityRepository.count();
        long totalWinners = winnerRepository.count();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalDraws", totalDraws);
        model.addAttribute("totalCharities", totalCharities);
        model.addAttribute("totalWinners", totalWinners);
        
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }

    @org.springframework.web.bind.annotation.PostMapping("/users/{id}/toggle")
    public String toggleUserStatus(@org.springframework.web.bind.annotation.PathVariable Long id) {
        com.golfcharity.entity.User user = userRepository.findById(id).orElseThrow();
        // Prevent admin from banning themselves
        if (user.getRole() != com.golfcharity.entity.Role.ADMIN) {
            user.setEnabled(!user.isEnabled());
            userRepository.save(user);
            
            if (!user.isEnabled()) {
                emailService.sendAccountBannedEmail(user.getEmail());
            }
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/draws")
    public String manageDraws(Model model) {
        model.addAttribute("draws", drawRepository.findAll());
        return "admin/draws";
    }

    @org.springframework.web.bind.annotation.PostMapping("/draws/init")
    public String initDraw() {
        com.golfcharity.entity.Draw draw = com.golfcharity.entity.Draw.builder()
            .month(java.time.LocalDate.now().getMonthValue())
            .year(java.time.LocalDate.now().getYear())
            .status(com.golfcharity.entity.DrawStatus.SIMULATION)
            .mode(com.golfcharity.entity.DrawMode.RANDOM)
            .build();
        drawRepository.save(draw);
        return "redirect:/admin/draws";
    }
    
    @org.springframework.web.bind.annotation.PostMapping("/draws/{id}/run")
    public String runSimulation(@org.springframework.web.bind.annotation.PathVariable Long id) {
        com.golfcharity.entity.Draw draw = drawRepository.findById(id).orElseThrow();
        if (draw.getStatus() == com.golfcharity.entity.DrawStatus.PUBLISHED) {
            return "redirect:/admin/draws";
        }
        
        draw.setStatus(com.golfcharity.entity.DrawStatus.PUBLISHED);
        draw.setPublishedAt(java.time.LocalDateTime.now());
        drawRepository.save(draw);

        java.util.List<com.golfcharity.entity.User> eligibleUsers = userRepository.findAll().stream()
            .filter(u -> u.getRole() == com.golfcharity.entity.Role.SUBSCRIBER && u.isEnabled())
            .toList();

        if (!eligibleUsers.isEmpty()) {
            com.golfcharity.entity.User randomWinner = eligibleUsers.get(new java.util.Random().nextInt(eligibleUsers.size()));
            
            com.golfcharity.entity.DrawResult dr = com.golfcharity.entity.DrawResult.builder()
                .draw(draw)
                .winningNumbers("RANDOM")
                .matchType(5)
                .prizeAmount(1000.0)
                .build();
            drawResultRepository.save(dr);

            com.golfcharity.entity.Winner winner = com.golfcharity.entity.Winner.builder()
                .user(randomWinner)
                .drawResult(dr)
                .verificationStatus(com.golfcharity.entity.WinnerStatus.PENDING_VERIFICATION)
                .paymentStatus(com.golfcharity.entity.WinnerStatus.PENDING_VERIFICATION)
                .build();
            winnerRepository.save(winner);
        }

        return "redirect:/admin/draws";
    }

    @GetMapping("/charities")
    public String manageCharities(Model model) {
        model.addAttribute("charities", charityRepository.findAll());
        return "admin/charities";
    }

    @org.springframework.web.bind.annotation.PostMapping("/charities/add")
    public String addCharity(@org.springframework.web.bind.annotation.RequestParam String name, @org.springframework.web.bind.annotation.RequestParam String description) {
        com.golfcharity.entity.Charity charity = com.golfcharity.entity.Charity.builder()
            .name(name)
            .description(description)
            .featured(false)
            .build();
        charityRepository.save(charity);
        return "redirect:/admin/charities";
    }

    @org.springframework.web.bind.annotation.PostMapping("/charities/{id}/toggle-feature")
    public String toggleCharityFeature(@org.springframework.web.bind.annotation.PathVariable Long id) {
        com.golfcharity.entity.Charity charity = charityRepository.findById(id).orElseThrow();
        charity.setFeatured(!charity.isFeatured());
        charityRepository.save(charity);
        return "redirect:/admin/charities";
    }

    @org.springframework.transaction.annotation.Transactional
    @org.springframework.web.bind.annotation.PostMapping("/charities/{id}/delete")
    public String deleteCharity(@org.springframework.web.bind.annotation.PathVariable Long id) {
        Charity charity = charityRepository.findById(id).orElseThrow();
        
        // Cascade delete dependent records manually
        contributionRepository.deleteByCharityId(id);
        charityEventRepository.deleteByCharity(charity);
        
        charityRepository.delete(charity);
        return "redirect:/admin/charities";
    }

    @GetMapping("/winners")
    public String manageWinners(Model model) {
        model.addAttribute("winners", winnerRepository.findAll());
        return "admin/winners";
    }
}
