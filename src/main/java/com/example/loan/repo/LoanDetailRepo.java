package com.example.loan.repo;


import com.example.loan.model.LoanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanDetailRepo extends JpaRepository<LoanDetail, Long> {

}
