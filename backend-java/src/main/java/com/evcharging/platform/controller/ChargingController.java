package com.evcharging.platform.controller;

import com.evcharging.model.ChargingSession;
import com.evcharging.platform.service.SessionService;
import com.evcharging.platform.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/charging")
public class ChargingController {

    private final SessionService sessionService;
    private final PaymentService paymentService;

    public ChargingController(SessionService sessionService, PaymentService paymentService) {
        this.sessionService = sessionService;
        this.paymentService = paymentService;
    }

    /**
     * Töltés indítási kérés a felhasználótól.
     * Megvalósítja az Atomic State Check logikát: először a munkamenet PENDING státuszba kerül,
     * és a pénz csak akkor lesz zárolva, ha a fizikai oszlop válaszol.
     */
    @PostMapping("/start")
    public ResponseEntity<ChargingSession> startCharging(@RequestParam String chargePointId, @RequestParam String userId) {
        System.out.println("[API] Töltés indítási kérés érkezett. Oszlop: " + chargePointId + ", Felhasználó: " + userId);

        // 1. Munkamenet létrehozása PENDING státuszban (nincs még pénz zárolva!)
        ChargingSession session = sessionService.initiateSession(chargePointId);

        // 2. Itt a háttérben a WebSocketen keresztül elküldjük a parancsot a töltőnek,
        // és várunk a visszajelzésre. Ha megérkezik a státusz, megtörténik a pre-auth.

        return ResponseEntity.ok(session);
    }
}