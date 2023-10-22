package com.example.loan.service;


import com.example.loan.dto.CreateNewUserReq;
import com.example.loan.dto.CreateNewUserRes;
import com.example.loan.model.Users;
import com.example.loan.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {


    @Autowired
    private UsersRepo usersRepo;


    public CreateNewUserRes createNewUser(CreateNewUserReq createNewUserReq) {
        String name = createNewUserReq.getName();
        String pan = createNewUserReq.getPan();

        Optional<Users> optionalUsers = usersRepo.findByPan(pan);
        if(optionalUsers.isPresent())
            return CreateNewUserRes.builder().success(false).message("User with same pan number already exist").build();

        Users users = Users
                .builder()
                .name(name)
                .pan(pan)
                .build();

        usersRepo.save(users);

        return CreateNewUserRes.builder().success(true).message("User created").build();
    }

    public List<Users> getAllUsers() {
        return usersRepo.findAll();
    }
}
