package edu.rutmiit.demo.auctionrest.exception;

import edu.rutmiit.demo.auctioncontract.dto.ErrorResponse;
import edu.rutmiit.demo.auctioncontract.exception.ResourceNotFoundException;
import edu.rutmiit.demo.auctioncontract.exception.LotNotActiveException;
import edu.rutmiit.demo.auctioncontract.exception.BidTooLowException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final String BASE_PROBLEM_URI = "https://api.example.com/problems/";
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ErrorResponse(404, BASE_PROBLEM_URI + "resource-not-found", "Ресурс не найден", ex.getMessage(), 
                            req.getRequestURI(), Instant.now(), null));
    }
    
    @ExceptionHandler(LotNotActiveException.class)
    public ResponseEntity<ErrorResponse> handleLotNotActive(LotNotActiveException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            new ErrorResponse(409, BASE_PROBLEM_URI + "lot-not-active", "Лот не активен", ex.getMessage(), 
                            req.getRequestURI(), Instant.now(), null));
    }
    
    @ExceptionHandler(BidTooLowException.class)
    public ResponseEntity<ErrorResponse> handleBidTooLow(BidTooLowException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ErrorResponse(400, BASE_PROBLEM_URI + "bid-too-low", "Ставка слишком мала", ex.getMessage(), 
                            req.getRequestURI(), Instant.now(), null));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            new ErrorResponse(500, BASE_PROBLEM_URI + "internal-error", "Внутренняя ошибка сервера", 
                            "Произошла непредвиденная ошибка", req.getRequestURI(), Instant.now(), null));
    }
}