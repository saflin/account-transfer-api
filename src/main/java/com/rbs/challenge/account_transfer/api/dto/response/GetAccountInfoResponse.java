package com.rbs.challenge.account_transfer.api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class GetAccountInfoResponse {

     private String accountNumber;

     private BigDecimal accountBalance;
}
