package com.golfcharity.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "draws")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Draw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer month;
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DrawMode mode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DrawStatus status;

    private LocalDateTime publishedAt;
}
