package com.evcharging.repository;

import com.evcharging.model.ChargePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargePointRepository extends JpaRepository<ChargePoint, String> {
}
