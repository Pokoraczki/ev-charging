package com.evcharging.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "charging_sessions")
public class ChargingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String chargePointId;

    private LocalDateTime startTime = LocalDateTime.now();

    private LocalDateTime endTime;

    private Double totalKwh = 0.0;

    private Double totalCostHuf = 0.0;

    private String status = "PENDING";

    // Getterek és Setterek
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getChargePointId() { return chargePointId; }
    public void setChargePointId(String chargePointId) { this.chargePointId = chargePointId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
