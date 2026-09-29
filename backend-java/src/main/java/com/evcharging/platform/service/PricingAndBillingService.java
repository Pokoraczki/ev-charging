package com.evcharging.platform.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PricingAndBillingService {

    // Magyarországi általános ÁFA kulcs
    private static final BigDecimal VAT_RATE = new BigDecimal("0.27");

    public BillingResult calculateSessionCost(double kwhConsumed, double pricePerKwhHuf) {
        BigDecimal energyKwh = BigDecimal.valueOf(kwhConsumed);
        BigDecimal unitPrice = BigDecimal.valueOf(pricePerKwhHuf);

        // Nettó energia díj kiszámítása
        BigDecimal netAmount = energyKwh.multiply(unitPrice).setScale(2, RoundingMode.HALF_UP);

        // 27% ÁFA számítás
        BigDecimal vatAmount = netAmount.multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);

        // Bruttó végösszeg
        BigDecimal grossAmount = netAmount.add(vatAmount).setScale(2, RoundingMode.HALF_UP);

        // Magyar készpénzes/számlás 5 forintos kerekítés (opcionális, de NAV kompatibilis kerekítési alapelem)
        BigDecimal roundedGross = roundToNearestFive(grossAmount);

        return new BillingResult(netAmount, vatAmount, grossAmount, roundedGross);
    }

    private BigDecimal roundToNearestFive(BigDecimal amount) {
        BigDecimal remainder = amount.remainder(BigDecimal.valueOf(5));
        BigDecimal halfOfFive = BigDecimal.valueOf(2.5);
        if (remainder.compareTo(halfOfFive) >= 0) {
            return amount.add(BigDecimal.valueOf(5)).subtract(remainder).setScale(0, RoundingMode.HALF_UP);
        } else {
            return amount.subtract(remainder).setScale(0, RoundingMode.HALF_UP);
        }
    }

    public static class BillingResult {
        private final BigDecimal netAmount;
        private final BigDecimal vatAmount;
        private final BigDecimal grossAmount;
        private final BigDecimal roundedGross;

        public BillingResult(BigDecimal netAmount, BigDecimal vatAmount, BigDecimal grossAmount, BigDecimal roundedGross) {
            this.netAmount = netAmount;
            this.vatAmount = vatAmount;
            this.grossAmount = grossAmount;
            this.roundedGross = roundedGross;
        }

        // Getterek
        public BigDecimal getNetAmount() { return netAmount; }
        public BigDecimal getVatAmount() { return vatAmount; }
        public BigDecimal getGrossAmount() { return grossAmount; }
        public BigDecimal getRoundedGross() { return roundedGross; }
    }
}