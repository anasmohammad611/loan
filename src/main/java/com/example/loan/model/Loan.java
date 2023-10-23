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
@Table(name="loan")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

	@Id
	@GeneratedValue(generator="SEQ_loan_loan_id")
	@SequenceGenerator(name="SEQ_loan_loan_id",sequenceName="SEQ_loan_loan_id", allocationSize=1)
	@Column(name="loan_id")
	private long loanId;

	@OneToOne
	@JoinColumn(name = "fk_user_id", referencedColumnName = "user_id" )
	private Users users;

	private double loanAmt;
	private int loanTerm;
	private Status status;
}
