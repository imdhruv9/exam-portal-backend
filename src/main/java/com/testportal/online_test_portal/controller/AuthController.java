package com.testportal.online_test_portal.controller;

import com.testportal.online_test_portal.dto.LoginRequestDto;
import com.testportal.online_test_portal.dto.UserRegisterDto;
import com.testportal.online_test_portal.dto.UserResponseDto;
import com.testportal.online_test_portal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/admin")
    public ResponseEntity<UserResponseDto> registerAdmin(@Valid @RequestBody UserRegisterDto userRequestDto){
        UserResponseDto userResponseDto = userService.registerAdmin(userRequestDto);
        return  new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }
    @PostMapping("/register/user")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRegisterDto userRequestDto){
        UserResponseDto savedUser = userService.registerUser(userRequestDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        String   response = userService.login(loginRequestDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


}
