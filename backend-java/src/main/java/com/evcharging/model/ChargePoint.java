package com.evcharging.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "charge_points")
public class ChargePoint {

    @Id
    private String id; // pl. CP_NYIREGYHAZA_01

    @Column(nullable = false)
    private String vendor;

    @Column(nullable = false)
    private String model;

    private String status = "OFFLINE";

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getterek és Setterek
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}