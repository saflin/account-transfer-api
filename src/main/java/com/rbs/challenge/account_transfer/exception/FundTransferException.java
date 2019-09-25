package com.rbs.challenge.account_transfer.exception;

/**
 *  Represents fund transfer exception while processing transfer request.
 */
public class FundTransferException extends RuntimeException {

    public FundTransferException(final String message){
        super(message);
    }
}
