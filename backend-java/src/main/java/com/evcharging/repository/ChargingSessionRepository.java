package com.evcharging.repository;

import com.evcharging.model.ChargingSession;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.evcharging.model.ChargingSession;

@Repository
public interface ChargingSessionRepository extends JpaRepository<ChargingSession, Long> {

    // Pesszimista zárolás a tranzakció idejére, megakadályozva a ghost session ütközéseket
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ChargingSession s WHERE s.transactionId = :transactionId")
    Optional<ChargingSession> findByTransactionIdWithLock(@Param("transactionId") String transactionId);

    // ÚJ: Lekérdezés a PENDING státuszú és egy adott időpontnál régebbi sessionökhöz (Ghost session takarító)
    List<ChargingSession> findByStatusAndStartTimeBefore(ChargingSession.SessionStatus status, LocalDateTime time);
}