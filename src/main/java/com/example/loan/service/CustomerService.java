package com.example.loan.service;


import com.example.loan.dto.CreateNewUserReq;
import com.example.loan.dto.CreateNewUserRes;
import com.example.loan.dto.GetLoanDetailsReq;
import com.example.loan.dto.GetLoanDetailsRes;
import com.example.loan.model.Customer;
import com.example.loan.model.Loan;
import com.example.loan.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {


    @Autowired
    private CustomerRepo customerRepo;


    public CreateNewUserRes createNewUser(CreateNewUserReq createNewUserReq) {
        String name = createNewUserReq.getName();
        String pan = createNewUserReq.getPan();

        Optional<Customer> optionalUsers = getUsers(pan);
        if (optionalUsers.isPresent())
            return CreateNewUserRes.builder().success(false).message("User with same pan number already exist").build();

        Customer customer = Customer
                .builder()
                .name(name)
                .pan(pan)
                .build();

        customerRepo.save(customer);

        return CreateNewUserRes.builder().success(true).message("User created").build();
    }

    private Optional<Customer> getUsers(String pan) {
        Optional<Customer> optionalUsers = customerRepo.findByPan(pan);
        return optionalUsers;
    }

    public List<Customer> getAllCustomer() {
        return customerRepo.findAll();
    }

    public Customer getUserByPan(String pan) {
        Optional<Customer> optionalUsers = getUsers(pan);
        if (optionalUsers.isEmpty())
            throw new RuntimeException("User pan not found");

        return optionalUsers.get();
    }
}
