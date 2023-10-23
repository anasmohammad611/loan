package com.example.loan.controller;


import com.example.loan.dto.CreateNewUserReq;
import com.example.loan.dto.CreateNewUserRes;
import com.example.loan.service.CustomerService;
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

    @PostMapping("/createNewUser")
    public ResponseEntity<?> createNewUser(@RequestBody CreateNewUserReq createNewUserReq) {
        CreateNewUserRes createNewUserRes = customerService.createNewUser(createNewUserReq);
        return new ResponseEntity<>(createNewUserRes, HttpStatus.OK);
    }

}
