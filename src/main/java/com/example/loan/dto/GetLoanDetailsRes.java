package com.example.loan.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetLoanDetailsRes {
    private Boolean success;
    private String message;

    private List<ScheduledPaymentsDto> paymentsDto;
}
