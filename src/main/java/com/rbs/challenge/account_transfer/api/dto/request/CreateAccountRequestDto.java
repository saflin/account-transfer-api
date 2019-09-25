package com.rbs.challenge.account_transfer.api.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class CreateAccountRequestDto {

    @NotNull
    @NotEmpty
    private  String accountNumber;

    @NotNull
    @Min(value = 0, message = "Initial balance must be positive.")
    private BigDecimal initialBalance;

}
