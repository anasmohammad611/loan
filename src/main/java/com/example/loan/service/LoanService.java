package com.example.loan.service;


import com.example.loan.dto.*;
import com.example.loan.model.Customer;
import com.example.loan.model.Loan;
import com.example.loan.model.LoanDetail;
import com.example.loan.model.enums.Status;
import com.example.loan.repo.LoanDetailRepo;
import com.example.loan.repo.LoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


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

        Optional<Loan> optionalLoan = loanRepo.findByCustomer_PanAndStatusOrStatus(pan);
        if (optionalLoan.isPresent()) {
            Loan loan = optionalLoan.get();
            if (loan.getStatus().equals(Status.PENDING)) {
                return CreateLoanRes.builder().success(false).message("You already have a loan in a pending state").build();
            } else {
                return CreateLoanRes.builder().success(false).message("You already have a loan in a approved state").build();
            }
        }

        Customer user = customerService.getUserByPan(pan);

        Loan loan = Loan
                .builder()
                .loanAmt(loanAmt)
                .loanTerm(loanTermInMonths)
                .status(Status.PENDING)
                .customer(user)
                .createdAt(new Timestamp(new Date().getTime()))
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

        Optional<Loan> optionalLoan = loanRepo.findByCustomer_PanAndStatus(pan, String.valueOf(Status.PENDING));
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
                .build();
    }

    private void createLoanInstallmentDetails(Loan loan) {
        Double loanAmt = loan.getLoanAmt();
        int loanTerm = loan.getLoanTerm();

        Double instalment = loanAmt / loanTerm;
        LoanDetail loanDetail = LoanDetail
                .builder()
                .instalmentNumber(0)
                .dueDate(addNMonth(new Timestamp(new Date().getTime()), 1))
                .loan(loan)
                .instalmentAmt(instalment)
                .status(Status.PENDING)
                .build();

        loanDetailRepo.save(loanDetail);
    }

    private Timestamp addNMonth(Timestamp timestamp, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        calendar.add(Calendar.MONTH, n);

        return new Timestamp(calendar.getTimeInMillis());
    }

    public GetLoanDetailsRes getLoanDetails(GetLoanDetailsReq getLoanDetailsReq) {
        String pan = getLoanDetailsReq.getPan();

        Optional<Loan> optionalLoan = loanRepo.findByCustomer_PanAndLatestStatus(pan);
        if(optionalLoan.isEmpty()) {
            return GetLoanDetailsRes.builder().success(false).message("you dont have any loans").build();
        }
        Loan loan = optionalLoan.get();

        if(loan.getStatus().equals(Status.PENDING)) {
            return GetLoanDetailsRes.builder().success(true).message("Your loan is in a pending state").build();
        } else if(loan.getStatus().equals(Status.DENIED)) {
            return GetLoanDetailsRes.builder().success(true).message("Your loan has been denied.").build();
        } else if(loan.getStatus().equals(Status.PAID)) {
            return GetLoanDetailsRes.builder().success(true).message("Your loan has been Paid").build();
        }

        Optional<LoanDetail> optionalLoanDetail = loanDetailRepo.findLoanDetailByLoan_LoanId(loan.getLoanId());
        if(optionalLoanDetail.isEmpty()) {
            throw new RuntimeException("Unexpected error while fetching loan details");
        }
        LoanDetail loanDetail = optionalLoanDetail.get();

        List<ScheduledPaymentsDto> paymentsList = generateScheduledInstalments(loanDetail);

        return GetLoanDetailsRes
                .builder()
                .paymentsDto(paymentsList)
                .success(true)
                .message("check the details")
                .build();
    }

    private List<ScheduledPaymentsDto> generateScheduledInstalments(LoanDetail loanDetail) {
        List<ScheduledPaymentsDto> scheduledPaymentsDtos = new ArrayList<>();

        int scheduledInstalMents = loanDetail.getLoan().getLoanTerm() - loanDetail.getInstalmentNumber();
        for(int i = 0; i < scheduledInstalMents; i++) {
            scheduledPaymentsDtos.add(
                    ScheduledPaymentsDto
                            .builder()
                            .dueAmount(loanDetail.getInstalmentAmt())
                            .dueDate(convertToReadableDate(addNMonth(loanDetail.getDueDate(), i)))
                            .build()
            );
        }

        return scheduledPaymentsDtos;
    }

    private String convertToReadableDate(Timestamp timestamp) {
        return new SimpleDateFormat("d MMM yyyy").format(new Date(timestamp.getTime()));
    }

    public PayMyEMIRes payMyEMI(PayMyEMIReq payMyEMIReq) {
        Double amt = payMyEMIReq.getPayAmt();
        String pan = payMyEMIReq.getPan();

        Optional<Loan> optionalLoan = loanRepo.findByCustomer_PanAndStatus(pan, String.valueOf(Status.APPROVED));
        if(optionalLoan.isEmpty())
            throw new RuntimeException("Loan not found");
        Loan loan = optionalLoan.get();

        Optional<LoanDetail> optionalLoanDetail = loanDetailRepo.findLoanDetailByLoan_LoanId(loan.getLoanId());
        if(optionalLoanDetail.isEmpty()) {
            throw new RuntimeException("Unexpected error while fetching loan details");
        }
        LoanDetail loanDetail = optionalLoanDetail.get();

        if(amt < loanDetail.getInstalmentAmt())
            return PayMyEMIRes.builder().message("amount cannot be less than EMI").build();

        double totalDue = loanDetail.getInstalmentAmt() * loan.getLoanTerm() - (loanDetail.getInstalmentNumber() * loanDetail.getInstalmentAmt());
        if(amt > totalDue) {
            loanDetail.setStatus(Status.PAID);
            loanDetail.setDueDate(addNMonth(loanDetail.getDueDate(), loan.getLoanTerm()));
            loan.setStatus(Status.PAID);
            loanRepo.save(loan);
            loanDetailRepo.save(loanDetail);
            return PayMyEMIRes.builder().pan(pan).message("amount has been repaid. Any extra amount will be repaid").build();
        }

        int totalInstalmentsPaying = (int) (amt / loanDetail.getInstalmentAmt());
        loanDetail.setDueDate(addNMonth(loanDetail.getDueDate(), 1));
        loanDetail.setInstalmentNumber(loanDetail.getInstalmentNumber() + totalInstalmentsPaying);
        loanDetailRepo.save(loanDetail);

        return PayMyEMIRes
                .builder()
                .pan(pan)
                .remainingDues(generateScheduledInstalments(loanDetail))
                .message(totalInstalmentsPaying + " has been paid. Extra amount will be repaid")
                .build();
    }
}
