package com.example.loan.repo;


import com.example.loan.model.LoanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanDetailRepo extends JpaRepository<LoanDetail, Long> {

    Optional<LoanDetail> findLoanDetailByLoan_LoanId(long loanId);
}
