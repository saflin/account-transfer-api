package com.rbs.challenge.account_transfer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class TransferRequest {

    private String fromAccountNumber;

    private String toAccountNumber;

    private BigDecimal transferAmount;


    public boolean areAccountsSame() {
        return  getFromAccountNumber().equals(getToAccountNumber());
    }

    public boolean isTransferAmountValid(){
        return transferAmount.compareTo(BigDecimal.ZERO) > 0;
    }
}
