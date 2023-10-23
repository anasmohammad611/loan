package com.example.loan.controller;


import com.example.loan.dto.CreateLoanReq;
import com.example.loan.dto.CreateLoanRes;
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
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/createLoan")
    public ResponseEntity<?> createNewUser(@RequestBody CreateLoanReq createLoanReq) {
        CreateLoanRes createLoanRes = loanService.createNewLoan(createLoanReq);
        return new ResponseEntity<>(createLoanRes, HttpStatus.OK);
    }

}
