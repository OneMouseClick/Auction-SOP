package edu.rutmiit.demo.auctioncontract.exception;

import java.math.BigDecimal;

public class BidTooLowException extends RuntimeException {
    public BidTooLowException(BigDecimal bidAmount, BigDecimal minRequiredAmount) {
        super(String.format("Ставка %.2f ниже минимально допустимой (%.2f)", bidAmount, minRequiredAmount));
    }
}