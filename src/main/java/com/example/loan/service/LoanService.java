package com.example.loan.service;


import com.example.loan.dto.ApproveLoanReq;
import com.example.loan.dto.ApproveLoanRes;
import com.example.loan.dto.CreateLoanReq;
import com.example.loan.dto.CreateLoanRes;
import com.example.loan.model.Customer;
import com.example.loan.model.Loan;
import com.example.loan.model.LoanDetail;
import com.example.loan.model.enums.Status;
import com.example.loan.repo.LoanDetailRepo;
import com.example.loan.repo.LoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {


    @Autowired
    private LoanRepo loanRepo;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoanDetailRepo loanDetailRepo;


    public CreateLoanRes createNewLoan(CreateLoanReq createLoanReq) {
        String pan = createLoanReq.getPan();
        Double loanAmt = createLoanReq.getLoanAmt();
        int loanTermInMonths = createLoanReq.getLoanTerm();

        Optional<Loan> optionalLoan = loanRepo.findByCustomer_Pan(pan);
        if (optionalLoan.isPresent()) {
            Loan loan = optionalLoan.get();
            if (loan.getStatus().equals(Status.PENDING)) {
                return CreateLoanRes.builder().success(false).message("You already have a loan").build();
            }
        }

        Customer user = customerService.getUserByPan(pan);

        Loan loan = Loan
                .builder()
                .loanAmt(loanAmt)
                .loanTerm(loanTermInMonths)
                .status(Status.PENDING)
                .customer(user)
                .build();

        loanRepo.save(loan);

        return CreateLoanRes
                .builder()
                .success(true)
                .message("Loan has been sanctioned for you. Wait for the approval!")
                .build();
    }

    public List<Loan> getAllPendingLoans() {
        return loanRepo.findLoansByStatus(Status.PENDING);
    }

    public List<Loan> getAllApprovedLoans() {
        return loanRepo.findLoansByStatus(Status.APPROVED);
    }

    public List<Loan> getAllDeniedLoans() {
        return loanRepo.findLoansByStatus(Status.DENIED);
    }

    public List<Loan> getAllPaidLoans() {
        return loanRepo.findLoansByStatus(Status.PAID);
    }

    public ApproveLoanRes approveLoan(ApproveLoanReq approveLoanReq) {
        boolean approve = approveLoanReq.getApprove();
        String pan = approveLoanReq.getPan();

        Optional<Loan> optionalLoan = loanRepo.findByCustomer_Pan(pan);
        if (optionalLoan.isEmpty())
            throw new RuntimeException("Loan not found for the pan: " + pan);
        Loan loan = optionalLoan.get();

        if (approve) {
            loan.setStatus(Status.APPROVED);
            createLoanInstallmentDetails(loan);
        } else {
            loan.setStatus(Status.DENIED);
        }

        loanRepo.save(loan);
        return ApproveLoanRes
                .builder()
                .success(true)
                .message("Loan has been approved for you. Enjoy MF!")
                .build();
    }

    private void createLoanInstallmentDetails(Loan loan) {
        Double loanAmt = loan.getLoanAmt();
        int loanTerm = loan.getLoanTerm();

        Double instalment = loanAmt / loanTerm;
        LoanDetail loanDetail = LoanDetail
                .builder()
                .instalmentNumber(0)
                .dueDate(addOneMonth(new Timestamp(new Date().getTime())))
                .loan(loan)
                .instalmentAmt(instalment)
                .status(Status.PENDING)
                .build();

        loanDetailRepo.save(loanDetail);
    }

    private Timestamp addOneMonth(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        calendar.add(Calendar.MONTH, 1);

        return new Timestamp(calendar.getTimeInMillis());
    }
}
