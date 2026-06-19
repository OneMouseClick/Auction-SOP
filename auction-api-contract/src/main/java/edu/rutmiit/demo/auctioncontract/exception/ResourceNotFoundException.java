package edu.rutmiit.demo.auctioncontract.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(String.format("%s с id=%s не найден", resourceName, resourceId));
    }
}