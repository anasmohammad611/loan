package com.example.loan.controller;


import com.example.loan.dto.ApproveLoanReq;
import com.example.loan.dto.ApproveLoanRes;
import com.example.loan.service.CustomerService;
import com.example.loan.service.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/getAllPendingLoans")
    public ResponseEntity<?> getAllPendingLoans() {
        return new ResponseEntity<>(loanService.getAllPendingLoans(), HttpStatus.OK);
    }

    @GetMapping("/getAllApprovedLoans")
    public ResponseEntity<?> getAllApprovedLoans() {
        return new ResponseEntity<>(loanService.getAllApprovedLoans(), HttpStatus.OK);
    }

    @GetMapping("/getAllDeniedLoans")
    public ResponseEntity<?> getAllDeniedLoans() {
        return new ResponseEntity<>(loanService.getAllDeniedLoans(), HttpStatus.OK);
    }

    @GetMapping("/getAllPaidLoans")
    public ResponseEntity<?> getAllPaidLoans() {
        return new ResponseEntity<>(loanService.getAllPaidLoans(), HttpStatus.OK);
    }

    @PostMapping("/approveLoan")
    public ResponseEntity<?> approveLoan(@RequestBody ApproveLoanReq approveLoanReq) {
        ApproveLoanRes approveLoanRes = loanService.approveLoan(approveLoanReq);
        return new ResponseEntity<>(approveLoanRes, HttpStatus.OK);
    }

    @GetMapping("/getAllCustomer")
    public ResponseEntity<?> getAllCustomer() {
        return new ResponseEntity<>(customerService.getAllCustomer(), HttpStatus.OK);
    }

}
