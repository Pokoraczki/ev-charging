package com.evcharging.platform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "charge_points")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChargePoint {

    @Id
    @Column(name = "charge_point_id", nullable = false, unique = true)
    private String chargePointId; // Pl. "CP-NYIREGYHZA-01"

    private String vendor;
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChargePointStatus status;

    public enum ChargePointStatus {
        OFFLINE, AVAILABLE, CHARGING, FAULTED
    }
}