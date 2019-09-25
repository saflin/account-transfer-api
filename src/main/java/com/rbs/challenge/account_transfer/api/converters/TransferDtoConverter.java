package com.rbs.challenge.account_transfer.api.converters;

import com.rbs.challenge.account_transfer.api.dto.request.TransferRequestDto;
import com.rbs.challenge.account_transfer.model.TransferRequest;
import org.springframework.stereotype.Component;

@Component
public class TransferDtoConverter {

    public TransferRequest convertDto(TransferRequestDto transferRequestDto) {
        return new TransferRequest(transferRequestDto.getFromAccountNumber(),
                transferRequestDto.getToAccountNumber(),
                transferRequestDto.getTransferAmount());
    }
}
