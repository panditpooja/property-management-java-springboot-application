package com.mycompany.property_management.service;

import com.mycompany.property_management.entity.UserEntity;
import com.mycompany.property_management.model.UserDto;
import com.mycompany.property_management.converter.UserConverter;
import com.mycompany.property_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserConverter userConverter;

    public UserDto registerUser(UserDto user){
        UserEntity entity = userConverter.convertModelToEntity(user);
        entity = userRepo.save(entity);
        user = userConverter.convertEntityToModel(entity);
        return user;
    }

    public String loginUser(String email, String password){
        try{
            Optional<UserEntity> entity = userRepo.findByOwnerEmail(email);
            Thread.sleep((long)(Math.random()*2000));
            if (entity.get().getPassword().equals(password)){
                return "User Logged In successfully";
            }else{
                return "Incorrect username or password";
            }
        } catch (Exception e) {
            return "User Not Registered";
        }


    }


}
