package com.rbs.challenge.account_transfer.api.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TransferRequestDto {

    @NotNull
    @NotEmpty
    private String fromAccountNumber;

    @NotNull
    @NotEmpty
    private String toAccountNumber;

    @NotNull
    @Min(value = 0, message = "Amount to be transfered must be greater than 0.")
    private BigDecimal transferAmount;


    @JsonCreator
    public TransferRequestDto(@JsonProperty("fromAccountNumber") String fromAccountNumber,
                    @JsonProperty("toAccountNumber") String toAccountNumber,
                    @JsonProperty("transferAmount") BigDecimal transferAmount) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.transferAmount = transferAmount;
    }
}
