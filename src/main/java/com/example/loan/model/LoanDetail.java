package com.example.loan.model;


import com.example.loan.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The type User detail.
 */
@Entity
@Table(name = "loan_detail")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanDetail {

    @Id
    @GeneratedValue(generator = "SEQ_loan_detail_loan_detail_id")
    @SequenceGenerator(name = "SEQ_loan_detail_loan_detail_id", sequenceName = "SEQ_loan_detail_loan_detail_id", allocationSize = 1)
    @Column(name = "loan_detail_id")
    private long loanDetailId;

    @ManyToOne
    @JoinColumn(name = "fk_loan_id", referencedColumnName = "loan_id")
    private Loan loan;

    private Timestamp dueDate;

    @Enumerated(EnumType.STRING)
    private Status status;
    private int instalmentNumber;
    private Double instalmentAmt;
}
