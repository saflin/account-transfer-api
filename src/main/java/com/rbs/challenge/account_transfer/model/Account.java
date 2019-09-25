package com.rbs.challenge.account_transfer.model;

import com.rbs.challenge.account_transfer.exception.InSufficientFundException;
import com.rbs.challenge.account_transfer.exception.InvalidAmountException;
import lombok.Data;

import java.math.BigDecimal;

import static java.lang.String.format;

@Data
public class Account {

    private final String accountNumber;

    private volatile BigDecimal balance;

    public Account(final String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = BigDecimal.ZERO;
    }

    public Account(final String accountNumber, final BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public synchronized void deposit(final BigDecimal amount){
        if(isValidAmount(amount)){
            balance = balance.add(amount);
        }
    }

    public synchronized void withdraw(final BigDecimal amount) throws InSufficientFundException {
        if(isValidAmount(amount)){
            BigDecimal newBalance = balance.subtract(amount);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0){
                throw new InSufficientFundException(format("Insufficient balance in account : %s, Unable to withdraw amount: %s",
                        accountNumber, amount));
            }else {
                balance = newBalance;
            }
        }
    }

    private boolean isValidAmount(BigDecimal amount) {
        if(amount == null || (amount.compareTo(BigDecimal.ZERO) < 0)){
            throw new InvalidAmountException("Amount can't be null or negative");
        }
        return true;
    }
}
