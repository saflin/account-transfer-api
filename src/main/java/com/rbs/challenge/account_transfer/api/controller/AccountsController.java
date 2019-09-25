package com.rbs.challenge.account_transfer.api.controller;

import com.rbs.challenge.account_transfer.api.converters.AccountDTOConverter;
import com.rbs.challenge.account_transfer.api.dto.request.CreateAccountRequestDto;
import com.rbs.challenge.account_transfer.api.dto.response.GetAccountInfoResponse;
import com.rbs.challenge.account_transfer.exception.DuplicateAccountException;
import com.rbs.challenge.account_transfer.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v1/accounts")
public class AccountsController {

    private final AccountService accountsService;

    private final AccountDTOConverter converter;

    @Autowired
    public AccountsController(final AccountService accountsService, final AccountDTOConverter converter) {
        this.accountsService = accountsService;
        this.converter = converter;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAccount(@RequestBody @Valid CreateAccountRequestDto requestDTO) {
        log.info("Creating account {}", requestDTO);
        try {
            this.accountsService.createAccount(converter.toAccount(requestDTO));
        } catch (DuplicateAccountException daie) {
            return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Account Created",HttpStatus.CREATED);
    }

    @GetMapping(path = "/{accountNumber}")
    public GetAccountInfoResponse getAccount(@PathVariable String accountNumber) {
        log.info("Retrieving account for number {}", accountNumber);
        return converter.toDto(accountsService.getAccount(accountNumber));
    }
}
