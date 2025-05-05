package com.testportal.online_test_portal.mapper;
import com.testportal.online_test_portal.dto.UserUpdateDto;
import com.testportal.online_test_portal.entity.User;
import io.micrometer.common.util.StringUtils;

public class UserMapper {


    public static void updateUserFromDto(UserUpdateDto dto, User user) {
        if (StringUtils.isNotBlank(dto.getFirstName())) user.setFirstName(dto.getFirstName());
        if (StringUtils.isNotBlank(dto.getLastName())) user.setLastName(dto.getLastName());
        if (StringUtils.isNotBlank(dto.getUsername())) user.setUsername(dto.getUsername());
        if (StringUtils.isNotBlank(dto.getEmail())) user.setEmail(dto.getEmail());
    }
}

