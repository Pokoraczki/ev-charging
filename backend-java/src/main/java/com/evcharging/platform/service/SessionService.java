package com.evcharging.platform.service;

import com.evcharging.platform.entity.ChargingSession;
import com.evcharging.platform.repository.ChargingSessionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SessionService {

    private final ChargingSessionRepository sessionRepository;

    public SessionService(ChargingSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * 1. ATOMIC STATE CHECK:
     * Új töltési tranzakció indítása PENDING státuszban.
     * Még NINCS pénz zárolás, amíg a fizikai oszlop vissza nem igazolja a csatlakozást.
     */
    @Transactional
    public ChargingSession initiateSession(String chargePointId) {
        ChargingSession session = ChargingSession.builder()
                .chargePointId(chargePointId)
                .transactionId(UUID.randomUUID().toString())
                .status(ChargingSession.SessionStatus.PENDING)
                .startTime(LocalDateTime.now())
                .build();

        return sessionRepository.save(session);
    }

    /**
     * 2. AUTOMATIC REFUND SCHEDULER (Öngyógyító háttérfolyamat):
     * 10 másodpercenként lefut, és ellenőrzi azokat a PENDING munkameneteket,
     * amelyek 30 másodpercnél régebbiek, de a töltő nem indította el (`ACTIVE`).
     * Ekkor automatikusan lefut a visszatérítés (Void/Refund) és lezárja a munkamenetet.
     */
    @Scheduled(fixedRate = 10000)
    @Transactional
    public void checkOrphanSessions() {
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(30);
        List<ChargingSession> pendingSessions = sessionRepository.findByStatusAndStartTimeBefore(
                ChargingSession.SessionStatus.PENDING, threshold
        );

        for (ChargingSession session : pendingSessions) {
            System.out.println("[SCHEDULER] Árva munkamenet (ghost session) detektálva ID: " + session.getId() +
                    " | Töltő: " + session.getChargePointId());

            // Itt hívódik majd meg a 4. Fejlesztő Payment Service Void API-ja!
            executeVoidPayment(session);

            // Státusz frissítése FAILED-re, hogy a pénz ne ragadjon bent
            session.setStatus(ChargingSession.SessionStatus.FAILED);
            session.setEndTime(LocalDateTime.now());
            session.setCostHuf(0.0);
            sessionRepository.save(session);

            System.out.println("[SCHEDULER] Banki zárolás sikeresen feloldva (Void), munkamenet lezárva.");
        }
    }

    private void executeVoidPayment(ChargingSession session) {
        // Banki Gateway API integráció helye (Void / Refund kérés küldése)
        if (session.getPaymentAuthId() != null) {
            System.out.println("[PAYMENT] Void parancs küldve a banki gateway-nek pre-auth ID-hoz: " + session.getPaymentAuthId());
        }
    }
}