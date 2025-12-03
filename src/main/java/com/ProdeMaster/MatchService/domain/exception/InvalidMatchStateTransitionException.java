package com.ProdeMaster.MatchService.domain.exception;

public class InvalidMatchStateTransitionException extends RuntimeException {
    public InvalidMatchStateTransitionException(String message) {
        super(message);
    }
}
