package com.example.loan.controller;


import com.example.loan.dto.CreateNewUserReq;
import com.example.loan.dto.CreateNewUserRes;
import com.example.loan.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@Slf4j
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/createNewUser")
    public ResponseEntity<?> createNewUser(@RequestBody CreateNewUserReq createNewUserReq) {
        CreateNewUserRes createNewUserRes = usersService.createNewUser(createNewUserReq);
        return new ResponseEntity<>(createNewUserRes, HttpStatus.OK);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(usersService.getAllUsers(), HttpStatus.OK);
    }
}
