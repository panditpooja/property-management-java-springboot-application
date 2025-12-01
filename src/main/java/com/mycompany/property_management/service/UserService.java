package com.mycompany.property_management.service;

import com.mycompany.property_management.config.JwtTokenUtil;
import com.mycompany.property_management.entity.AddressEntity;
import com.mycompany.property_management.entity.Role;
import com.mycompany.property_management.entity.UserEntity;
import com.mycompany.property_management.exception.BusinessException;
import com.mycompany.property_management.exception.ErrorModel;
import com.mycompany.property_management.model.UserDto;
import com.mycompany.property_management.converter.UserConverter;
import com.mycompany.property_management.repository.AddressRepository;
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

    @Autowired
    private AddressRepository addressRepo;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public UserDto registerUser(UserDto user){
        Optional<UserEntity> userExists = userRepo.findByOwnerEmail(user.getOwnerEmail());
        if (userExists.isEmpty()) {
            AddressEntity addressEntity = new AddressEntity();
            UserEntity entity = userConverter.convertModelToEntity(user);

            // Set default roles if not provided
            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                // Default to USER role only
                entity.setRoles(new ArrayList<>(List.of(Role.USER)));
            } else {
                // Use provided roles (e.g., ["USER", "PROPERTY_OWNER"])
                entity.setRoles(user.getRoles());
            }

            addressEntity.setHouseNo(user.getHouseNo());
            addressEntity.setStreet(user.getStreet());
            addressEntity.setPostalCode(user.getPostalCode());
            addressEntity.setCountry(user.getCountry());
            entity = userRepo.save(entity);
            addressEntity.setUserEntity(entity);
            addressRepo.save(addressEntity);
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

                // SECURELY check if the plain-text password matches the stored hash
                if (passwordEncoder.matches(password, storedHashedPassword)) {
                    // Generate JWT token with user's roles
                    // Note: user.getRoles() returns List<Role> (enum objects), but we need List<String>
                    // So we convert Role.USER → "USER", Role.ADMIN → "ADMIN", etc.
                    List<String> roleNames = user.getRoles().stream()
                            .map(Role::name)
                            .collect(java.util.stream.Collectors.toList());
                    return jwtTokenUtil.generateToken(email, roleNames);
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
