package com.testportal.online_test_portal.service;

import com.testportal.online_test_portal.dto.*;

public interface UserService {

    UserResponseDto registerUser(UserRegisterDto userRequestDto);

    UserResponseDto registerAdmin(UserRegisterDto userRequestDto);

    String login(LoginRequestDto loginRequestDto);

    UserProfileDto getUserById(Long id);

    UserProfileDto updateUserProfile(UserUpdateDto userupdatedto);

    void deleteUserById(Long id);

    void updatePassword(PasswordUpdateDto passwordUpdateDto);
}
