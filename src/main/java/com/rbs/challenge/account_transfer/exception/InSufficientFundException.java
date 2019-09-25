package com.rbs.challenge.account_transfer.exception;

/**
 * Exception class to represent InSufficient Balance in the account.
 */
public class InSufficientFundException extends RuntimeException {
    public InSufficientFundException(final String message) {
        super(message);
    }
}
