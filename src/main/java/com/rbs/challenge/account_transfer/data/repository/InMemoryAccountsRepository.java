package com.rbs.challenge.account_transfer.data.repository;

import com.rbs.challenge.account_transfer.model.Account;
import com.rbs.challenge.account_transfer.exception.DuplicateAccountException;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAccountsRepository implements AccountsRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void createAccount(Account account) throws DuplicateAccountException {
        Account previousAccount = accounts.putIfAbsent(account.getAccountNumber(), account);
        if (previousAccount != null) {
            throw new DuplicateAccountException(
                    "Account with number: " + account.getAccountNumber() + " already exists!");
        }
    }

    @Override
    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    @Override
    public void clearAccounts() {
        accounts.clear();
    }
}
