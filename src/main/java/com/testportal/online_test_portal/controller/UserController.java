package com.testportal.online_test_portal.controller;

import com.testportal.online_test_portal.dto.UserRequestDto;
import com.testportal.online_test_portal.dto.UserResponseDto;
import com.testportal.online_test_portal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUserController(@Valid @RequestBody UserRequestDto userRequestDto){
        UserResponseDto savedUser = userService.registerUser(userRequestDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }


}
