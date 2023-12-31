package com.example.loan.repo;


import com.example.loan.model.Loan;
import com.example.loan.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepo extends JpaRepository<Loan, Long> {

    @Query(value = "select * from loan inner join public.customer c on c.customer_id = loan.fk_customer_id where c.pan = :pan and (status in ('PENDING', 'APPROVED'))", nativeQuery = true)
    Optional<Loan> findByCustomer_PanAndStatusOrStatus(String pan);

    List<Loan> findLoansByStatus(Status status);


    @Query(value = "select * from loan inner join public.customer c on c.customer_id = loan.fk_customer_id where c.pan = :pan and loan.status = :status", nativeQuery = true)
    Optional<Loan> findByCustomer_PanAndStatus(String pan, String status);

    @Query(value = "select * from loan inner join public.customer c on c.customer_id = loan.fk_customer_id where c.pan = :pan order by created_at desc limit 1", nativeQuery = true)
    Optional<Loan> findByCustomer_PanAndLatestStatus(String pan);
}
