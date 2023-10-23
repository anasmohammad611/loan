package com.example.loan.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledPaymentsDto {
    private String dueDate;
    private Double dueAmount;
}
