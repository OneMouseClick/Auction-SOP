package edu.rutmiit.demo.auctioncontract.exception;

public class LotNotActiveException extends RuntimeException {
    public LotNotActiveException(Long lotId) {
        super(String.format("Лот с id=%s не активен. Текущий статус запрещает размещение ставок.", lotId));
    }
}