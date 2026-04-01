package com.golfcharity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "charity_contributions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharityContribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charity_id", nullable = false)
    private Charity charity;

    @Column(nullable = false)
    private Double percentage; // min 10%

    private Double amount;
}
