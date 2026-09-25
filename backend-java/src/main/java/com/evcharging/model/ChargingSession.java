package com.evcharging.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "charging_sessions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChargingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String chargePointId;

    @Column(nullable = false)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    private Double kwhConsumed;
    private Double costHuf;

    private String paymentAuthId; // Banki pre-auth azonosító a visszatérítéshez/voidhoz

    public enum SessionStatus {
        PENDING,   // Még nincs pénz zárolva, várjuk a töltő oszlop visszajelzését (Atomic State Check)
        ACTIVE,    // Stabil kapcsolat, folyamatban a töltés
        COMPLETED, // Sikeresen lezárva
        FAILED     // Időtúllépés vagy hiba miatt megszakítva / visszatérítve
    }
}