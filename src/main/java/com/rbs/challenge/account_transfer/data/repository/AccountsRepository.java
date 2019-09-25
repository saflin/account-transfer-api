package com.rbs.challenge.account_transfer.data.repository;

import com.rbs.challenge.account_transfer.model.Account;
import com.rbs.challenge.account_transfer.exception.DuplicateAccountException;

public interface AccountsRepository {

    void createAccount(Account account) throws DuplicateAccountException;

    Account getAccount(String accountNumber);

    void clearAccounts();
}
