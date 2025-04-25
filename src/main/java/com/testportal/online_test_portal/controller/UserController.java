package com.testportal.online_test_portal.controller;

import com.testportal.online_test_portal.dto.PasswordUpdateDto;
import com.testportal.online_test_portal.dto.UserProfileDto;
import com.testportal.online_test_portal.dto.UserUpdateDto;
import com.testportal.online_test_portal.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getUserById(@PathVariable @Min(1) Long id){
        UserProfileDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PatchMapping
    public ResponseEntity<UserProfileDto> updateUserProfile(@RequestBody @Valid UserUpdateDto userUpdateDto){
        UserProfileDto user = userService.updateUserProfile(userUpdateDto);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable @Min(1) Long id){
        userService.deleteUserById(id);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("password-update")
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid PasswordUpdateDto passwordUpdateDto){
        userService.updatePassword(passwordUpdateDto);
       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
