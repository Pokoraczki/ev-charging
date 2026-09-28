package com.evcharging.platform.repository;

import com.evcharging.platform.entity.ChargingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChargingSessionRepository extends JpaRepository<ChargingSession, Long> {
    List<ChargingSession> findByStatusAndStartTimeBefore(ChargingSession.SessionStatus status, LocalDateTime time);
}