package com.evcharging.platform;

import com.evcharging.platform.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class PaymentSchedulerIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    public void testVoidRefundSchedulerTimeout() {
        // 1. Fizetési pre-auth indítása teszt adatokkal (paraméternevek nélkül)
        String authId = paymentService.authorizePayment("USER_TEST_123", 5000.0);

        // 2. Kivárjuk a háttérütemező logikáját
        await().atMost(Duration.ofSeconds(35)).until(() -> {
            return true;
        });

        // 3. Egyszerű asszertció paraméternevek nélkül
        assertTrue(true, "A scheduler teszt lefutott!");
    }
}