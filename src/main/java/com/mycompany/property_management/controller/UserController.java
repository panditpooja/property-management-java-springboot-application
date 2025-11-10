package com.mycompany.property_management.controller;

import com.mycompany.property_management.model.UserDto;
import com.mycompany.property_management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/registerUser")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto user){
        UserDto userDto = userService.registerUser(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/loginUser")
    public ResponseEntity<String> loginUser(@RequestBody UserDto user){
        String result = userService.loginUser(user.getOwnerEmail(),user.getPassword());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
