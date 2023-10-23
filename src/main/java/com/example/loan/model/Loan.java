package com.example.loan.model;


import com.example.loan.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


/**
 * The type User detail.
 */
@Entity
@Table(name = "loan")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(generator = "SEQ_loan_loan_id")
    @SequenceGenerator(name = "SEQ_loan_loan_id", sequenceName = "SEQ_loan_loan_id", allocationSize = 1)
    @Column(name = "loan_id")
    private long loanId;

    @OneToOne
    @JoinColumn(name = "fk_customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    private double loanAmt;
    private int loanTerm;

    @Enumerated(EnumType.STRING)
    private Status status;
}
