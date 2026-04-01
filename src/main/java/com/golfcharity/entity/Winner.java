package com.golfcharity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "winners")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Winner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "draw_result_id", nullable = false)
    private DrawResult drawResult;

    private String proofUrl;

    @Enumerated(EnumType.STRING)
    @Column(name="verification_status")
    private WinnerStatus verificationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_status")
    private WinnerStatus paymentStatus;
}
