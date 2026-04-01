package com.golfcharity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "draw_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrawEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "draw_id", nullable = false)
    private Draw draw;

    @Column(nullable = false)
    private String scores; // The calculated average/combo of scores user entered
}
