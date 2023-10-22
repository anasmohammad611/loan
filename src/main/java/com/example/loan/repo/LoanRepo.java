package com.example.loan.repo;


import com.example.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepo extends JpaRepository<Loan, Long> {

    Optional<Loan> findByUsers_Pan(String pan);
}
