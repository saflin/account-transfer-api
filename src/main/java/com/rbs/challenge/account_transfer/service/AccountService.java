package com.rbs.challenge.account_transfer.service;

import com.rbs.challenge.account_transfer.model.Account;
import com.rbs.challenge.account_transfer.data.repository.AccountsRepository;
import com.rbs.challenge.account_transfer.exception.DuplicateAccountException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private AccountsRepository accountsRepository;

    public AccountService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public void createAccount(final Account account) throws DuplicateAccountException {
        accountsRepository.createAccount(account);
    }

    public Account getAccount(final String accountNumber) {
        return accountsRepository.getAccount(accountNumber);
    }
}
