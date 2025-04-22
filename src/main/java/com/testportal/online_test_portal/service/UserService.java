package com.testportal.online_test_portal.service;

import com.testportal.online_test_portal.dto.UserRequestDto;
import com.testportal.online_test_portal.dto.UserResponseDto;

public interface UserService {

    UserResponseDto registerUser(UserRequestDto userRequestDto);
}
