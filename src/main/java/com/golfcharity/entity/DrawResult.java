package com.golfcharity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "draw_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrawResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "draw_id", nullable = false)
    private Draw draw;

    @Column(nullable = false)
    private String winningNumbers; // Store as comma-separated or JSON

    private Integer matchType; // 5, 4, or 3
    private Double prizeAmount;
}
