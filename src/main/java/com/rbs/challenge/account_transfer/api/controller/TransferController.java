package com.rbs.challenge.account_transfer.api.controller;

import com.rbs.challenge.account_transfer.api.converters.TransferDtoConverter;
import com.rbs.challenge.account_transfer.api.dto.request.TransferRequestDto;
import com.rbs.challenge.account_transfer.exception.AccountNotFoundException;
import com.rbs.challenge.account_transfer.exception.FundTransferException;
import com.rbs.challenge.account_transfer.exception.InSufficientFundException;
import com.rbs.challenge.account_transfer.exception.InValidTransferRequestException;
import com.rbs.challenge.account_transfer.service.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Rest API for Fund transfer between accounts
 */
@Slf4j
@RestController
@RequestMapping("/v1/transfers")
public class TransferController {

    private TransferDtoConverter transferDtoConverter;

    private TransferService transferService;

    @Autowired
    public TransferController(final TransferService transferService,
                              final TransferDtoConverter transferDtoConverter) {
        this.transferService = transferService;
        this.transferDtoConverter = transferDtoConverter;
    }

    /**
     * Transfer's funds between two existing accounts.
     *
     * @param transferRequest
     * @return ResponseEntity
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> transferFund(@RequestBody @Valid TransferRequestDto transferRequest) {
        log.info("Received transfer request : {}", transferRequest);
        transferService.transferFund(transferDtoConverter.convertDto(transferRequest));
        log.info("Sucessfully processed transfer request : {}", transferRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
//
//    /**
//     * Exception handler for bad requests
//     *
//     * @param request
//     * @param exception
//     * @return ResponseEntity
//     */
//    @ExceptionHandler({InSufficientFundException.class, AccountNotFoundException.class,
//            InValidTransferRequestException.class})
//    public ResponseEntity<?> handleClientSideExceptions(HttpServletRequest request, Exception exception) {
//        log.error("Cancelling transfer request. Reason : {}", exception.getMessage());
//        return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * Exception handler for server side exceptions
//     *
//     * @param request
//     * @param exception
//     * @return ResponseEntity
//     */
//    @ExceptionHandler(FundTransferException.class)
//    public ResponseEntity<?> handleFundTransferxceptions(HttpServletRequest request, FundTransferException exception) {
//        log.error("Failed to process transfer request. Reason : {}", exception.getMessage());
//        return new ResponseEntity(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
