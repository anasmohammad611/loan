package com.example.loan.controller;


import com.example.loan.dto.*;
import com.example.loan.service.CustomerService;
import com.example.loan.service.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoanService loanService;

    @PostMapping("/createNewUser")
    public ResponseEntity<?> createNewUser(@RequestBody CreateNewUserReq createNewUserReq) {
        CreateNewUserRes createNewUserRes = customerService.createNewUser(createNewUserReq);
        return new ResponseEntity<>(createNewUserRes, HttpStatus.OK);
    }

    @PostMapping("/getLoanDetails")
    public ResponseEntity<?> getLoanDetails(@RequestBody GetLoanDetailsReq getLoanDetailsReq) {
        GetLoanDetailsRes getLoanDetailsRes = loanService.getLoanDetails(getLoanDetailsReq);
        return new ResponseEntity<>(getLoanDetailsRes, HttpStatus.OK);
    }

    @PostMapping("/payMyEMI")
    public ResponseEntity<?> payMyEMI(@RequestBody PayMyEMIReq payMyEMIReq) {
        PayMyEMIRes payMyEMIRes = loanService.payMyEMI(payMyEMIReq);
        return new ResponseEntity<>(payMyEMIRes, HttpStatus.OK);
    }

}
