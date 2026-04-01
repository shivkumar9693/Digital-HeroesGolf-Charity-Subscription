package com.golfcharity.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "charity_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharityEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charity_id", nullable = false)
    private Charity charity;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate eventDate;
}
