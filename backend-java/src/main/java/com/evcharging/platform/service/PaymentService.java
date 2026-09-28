package com.evcharging.platform.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PaymentService {

    /**
     * Banki Pre-Authorization (összeg zárolása a kártyán a töltés előtt).
     * Csak akkor hívódik meg, ha a fizikai töltőoszlop WebSocketen jelezte, hogy stabil a kapcsolat!
     */
    public String authorizePayment(String userId, double amountHuf) {
        System.out.println("[PAYMENT] Pre-auth kérés indítva " + userId + " részére. Összeg: " + amountHuf + " HUF");

        // Valós banki API hívás helye (pl. Barion, SimplePay, Erste Gateway)
        String authId = "AUTH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        System.out.println("[PAYMENT] Összeg sikeresen zárolva. Pre-auth ID: " + authId);
        return authId;
    }

    /**
     * Azonnali zárolás feloldás (Void / Refund) ghost session vagy hiba esetén.
     */
    public boolean voidPayment(String paymentAuthId) {
        if (paymentAuthId == null || paymentAuthId.isEmpty()) {
            return false;
        }

        System.out.println("[PAYMENT] VOID / REFUND indítva a következő pre-auth azonosítóhoz: " + paymentAuthId);
        // Valós banki void API hívás...
        System.out.println("[PAYMENT] A zárolás sikeresen feloldva, a pénz nem ragadt bent a számlán.");
        return true;
    }
}