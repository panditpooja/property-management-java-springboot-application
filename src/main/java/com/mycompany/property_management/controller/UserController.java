package com.mycompany.property_management.controller;

import com.mycompany.property_management.model.UserDto;
import com.mycompany.property_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "This method is used for user registration")
    @PostMapping(path ="/registerUser", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto user){
        UserDto userDto = userService.registerUser(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @Operation(summary = "This method is used to login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "400", description = "Incorrect login credentials")
    })
    @PostMapping(path = "/loginUser", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<String> loginUser(@RequestBody UserDto user){
        String result = userService.loginUser(user.getOwnerEmail(),user.getPassword());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
