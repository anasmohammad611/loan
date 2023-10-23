package com.example.loan.service;


import com.example.loan.dto.CreateLoanReq;
import com.example.loan.dto.CreateLoanRes;
import com.example.loan.model.Loan;
import com.example.loan.model.Users;
import com.example.loan.model.enums.Status;
import com.example.loan.repo.LoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoanService {


    @Autowired
    private LoanRepo loanRepo;

    @Autowired
    private UsersService usersService;


    public CreateLoanRes createNewLoan(CreateLoanReq createLoanReq) {
        String pan = createLoanReq.getPan();
        Double loanAmt = createLoanReq.getLoanAmt();
        int loanTermInMonths = createLoanReq.getLoanTerm();

        Optional<Loan> optionalLoan = loanRepo.findByUsers_Pan(pan);
        if(optionalLoan.isPresent()) {
            Loan loan = optionalLoan.get();
            if(loan.getStatus().equals(Status.PENDING)) {
                return CreateLoanRes.builder().success(false).message("You already have a loan").build();
            }
        }

        Users user = usersService.getUserByPan(pan);

        Loan loan = Loan
                .builder()
                .loanAmt(loanAmt)
                .loanTerm(loanTermInMonths)
                .status(Status.PENDING)
                .users(user)
                .build();

        loanRepo.save(loan);

        return CreateLoanRes.builder().success(true).message("Loan has been sanctioned for you. Enjoy MF!").build();
    }
}
