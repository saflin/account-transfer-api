package com.rbs.challenge.account_transfer.service;

import com.rbs.challenge.account_transfer.exception.*;
import com.rbs.challenge.account_transfer.model.Account;
import com.rbs.challenge.account_transfer.model.TransferRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.lang.String.format;

@Slf4j
@Service
public class TransferService {

    private AccountService accountService;

    public TransferService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Transfer fund between two accounts.
     * Throws InValidTransferRequestException if from and to accounts are same.
     * Throws InValidTransferRequestException if transfer amount is <= 0.
     * Throws AccountNotFoundException if accounts doesnt exists
     * Throws InSufficientFundException if there is no fund available to withdraw.
     * Throws FundTransferException if transfer fails.
     *
     * @param transferRequest
     */
    public void transferFund(TransferRequest transferRequest) {

        if (transferRequest.areAccountsSame()) {
            throw new InValidTransferRequestException("Fund transfer to same account is not allowed.");
        }

        if (!transferRequest.isTransferAmountValid()) {
            throw new InValidTransferRequestException("Fund transfer amount should be greater than Zero.");
        }

        Account fromAccount = accountService.getAccount(transferRequest.getFromAccountNumber());
        if (fromAccount == null) {
            throw new AccountNotFoundException(format("Account with ID: %s doesnt exists.",
                    transferRequest.getToAccountNumber()));
        }

        Account toAccount = accountService.getAccount(transferRequest.getToAccountNumber());
        if (toAccount == null) {
            throw new AccountNotFoundException(format("Account with ID: %s doesnt exists.",
                    transferRequest.getToAccountNumber()));
        }
        transferFundThreadSafely(fromAccount, toAccount, transferRequest.getTransferAmount());
    }

    /**
     * Perform's fund transfer inside synchronised context.
     * Thread locking is done on account objects .
     * Account objects are sorted in predictive manner to avoid dead lock.
     *
     * @param fromAccount
     * @param toAccount
     * @param amount
     */
    private void transferFundThreadSafely(Account fromAccount, Account toAccount, BigDecimal amount) {
        //prevent dead lock by ordering the lock
        Object lock_1 = fromAccount.getAccountNumber().compareTo(toAccount.getAccountNumber()) < 0 ? fromAccount : toAccount;
        Object lock_2 = lock_1 != fromAccount ? fromAccount : toAccount;
        log.debug("Getting lock on lock 1 {} ", lock_1);
        synchronized (lock_1) {
            log.debug("Getting lock on lock 2 {} ", lock_2);
            synchronized (lock_2) {
                withdrawFund(fromAccount, amount);
                depositFund(fromAccount, toAccount, amount);
            }
        }

    }


    /**
     * Deposit amount to toAccount.
     * Rollback withdrawal from fromAccount, if deposit fails.
     * To avoid data race condition, this method should be invoked
     * after getting synchronised lock on fromAccount and toAccount.
     * See implementation of transferFundThreadSafely.
     *
     * @param fromAccount
     * @param toAccount
     * @param amount
     */
    private void depositFund(final Account fromAccount, final Account toAccount, final BigDecimal amount) {
        try {
            log.debug("Depositing amount:{} to account {}", amount, toAccount.getAccountNumber());
            toAccount.deposit(amount);
        }  catch (Exception ex) {
            log.error(format("Exception while depositing fund to account %s", toAccount.getAccountNumber()), ex);
            //rollback withdrawal. What if this fails?
            fromAccount.deposit(amount);
            throw new FundTransferException(format("Failed to transfer fund to Account: %s", toAccount.getAccountNumber()));
        }
    }

    /**
     * Withdraw's amount from fromAccount.
     * To avoid data race condition, this method should be invoked
     * after getting synchronised lock on fromAccount.
     * See implementation of transferFundThreadSafely.
     *
     * @param fromAccount
     * @param amount
     */
    private void withdrawFund(final Account fromAccount, final BigDecimal amount) {
        try {
            log.debug("Withdrawing amount:{} from account {}", amount, fromAccount.getAccountNumber());
            fromAccount.withdraw(amount);
        } catch (InSufficientFundException| InvalidAmountException ex) {
            log.error(format("Exception while withdrawing fund from account %s", fromAccount.getAccountNumber()), ex);
            throw ex;
        } catch (Exception ex){

        }
    }

}
