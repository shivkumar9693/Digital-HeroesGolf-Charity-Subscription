package com.golfcharity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prize_pools")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrizePool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "draw_id", nullable = false)
    private Draw draw;

    private Double totalPool;
    private Double fiveMatchPool; // 40%
    private Double fourMatchPool; // 35%
    private Double threeMatchPool; // 25%

    private Double jackpotCarryover;
}
