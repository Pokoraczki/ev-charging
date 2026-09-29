package com.evcharging.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ChargePoint {
    @Id
    private String id;
    private String status;

    // Getterek és setterek
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}