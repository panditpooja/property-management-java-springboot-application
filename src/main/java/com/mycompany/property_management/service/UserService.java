package com.mycompany.property_management.service;

//import com.mycompany.property_management.config.JwtTokenUtil;
import com.mycompany.property_management.entity.UserEntity;
import com.mycompany.property_management.exception.BusinessException;
import com.mycompany.property_management.exception.ErrorModel;
import com.mycompany.property_management.model.UserDto;
import com.mycompany.property_management.converter.UserConverter;
import com.mycompany.property_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;

    public UserDto registerUser(UserDto user){
        UserEntity entity = userConverter.convertModelToEntity(user);
        Optional<UserEntity> userExists = userRepo.findByOwnerEmail(entity.getOwnerEmail());
        if (userExists.isEmpty()) {
            entity = userRepo.save(entity);
            user = userConverter.convertEntityToModel(entity);
            return user;
        }
        List<ErrorModel> errors = new ArrayList<>();
        ErrorModel error = new ErrorModel();
        error.setCode("EXISTING_USER");
        error.setMessage("User already exists.");
        errors.add(error);
        throw new BusinessException(errors);
    }

    public String loginUser(String email, String password){
        try{
            Optional<UserEntity> entity = userRepo.findByOwnerEmail(email);
            Thread.sleep((long)(Math.random()*2000));
            if (entity.isPresent()){
                UserEntity user = entity.get();
                String storedHashedPassword = user.getPassword();

                // 3. SECURELY check if the plain-text password matches the stored hash
                if (passwordEncoder.matches(password, storedHashedPassword)) {
                    return "User Logged In successfully";
//                    return jwtTokenUtil.generateToken(user.getOwnerEmail());
                }
            }
            List<ErrorModel> errors = new ArrayList<>();
            ErrorModel error = new ErrorModel();
            error.setCode("INVALID_LOGIN");
            error.setMessage("Incorrect username or password");
            errors.add(error);
            throw new BusinessException(errors);

        } catch (InterruptedException e) {
            List<ErrorModel> errors = new ArrayList<>();
            ErrorModel error = new ErrorModel();
            error.setCode("INTERNAL_ERROR");
            error.setMessage("Request was interrupted");
            errors.add(error);
            throw new BusinessException(errors);
        }
    }

}
