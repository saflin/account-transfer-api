package com.rbs.challenge.account_transfer.api.converters;

import com.rbs.challenge.account_transfer.api.dto.request.CreateAccountRequestDto;
import com.rbs.challenge.account_transfer.api.dto.response.GetAccountInfoResponse;
import com.rbs.challenge.account_transfer.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountDTOConverter {

    public Account toAccount(CreateAccountRequestDto createAccountRequestDTO){
        return new Account(createAccountRequestDTO.getAccountNumber(),
                createAccountRequestDTO.getInitialBalance());
    }

    public GetAccountInfoResponse toDto(Account account){
        GetAccountInfoResponse response = new GetAccountInfoResponse();
        response.setAccountBalance(account.getBalance());
        response.setAccountNumber(account.getAccountNumber());
        return response;
    }
}
