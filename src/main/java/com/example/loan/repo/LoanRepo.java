package com.example.loan.repo;


import com.example.loan.model.Loan;
import com.example.loan.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepo extends JpaRepository<Loan, Long> {

    Optional<Loan> findByCustomer_Pan(String pan);

    List<Loan> findLoansByStatus(Status status);
}
