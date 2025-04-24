package com.testportal.online_test_portal.service.implementation;

import com.testportal.online_test_portal.dto.LoginRequestDto;
import com.testportal.online_test_portal.dto.UserRequestDto;
import com.testportal.online_test_portal.dto.UserResponseDto;
import com.testportal.online_test_portal.entity.User;
import com.testportal.online_test_portal.enums.Role;
import com.testportal.online_test_portal.exception.custom.DuplicateEntryException;
import com.testportal.online_test_portal.exception.custom.InvalidCredentialException;
import com.testportal.online_test_portal.exception.custom.UserNotFoundException;
import com.testportal.online_test_portal.repository.UserRepository;
import com.testportal.online_test_portal.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto){
        if(userRepository.existsByUsername(userRequestDto.getUsername())){
            throw new DuplicateEntryException("User already exist");
        }
        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());

        User user = User.builder()
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .username(userRequestDto.getUsername())
                .email(userRequestDto.getEmail())
                .password(encodedPassword)
                .role(Role.USER)
                .build();
                 User savedUser = userRepository.save(user);
        return UserResponseDto.builder()
                .email(savedUser.getEmail())
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .build();

    }
    @Override
    public UserResponseDto registerAdmin(UserRequestDto userRequestDto){
        if(userRepository.existsByUsername(userRequestDto.getUsername())){
            throw new DuplicateEntryException("User already exist");
        }
            String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());

        User user = User.builder()
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .username(userRequestDto.getUsername())
                .email(userRequestDto.getEmail())
                .password(encodedPassword)
                .role(Role.ADMIN)
                .build();
        User savedUser = userRepository.save(user);
        return UserResponseDto.builder()
                .email(savedUser.getEmail())
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .build();

    }

    @Override
    public String login(LoginRequestDto loginRequestDto){
       Optional<User> optionalUser = userRepository.findByUsername(loginRequestDto.getUsername());
       User user = optionalUser.orElseThrow(()-> new  UserNotFoundException("User not found"));
       if(passwordEncoder.matches(loginRequestDto.getPassword(),user.getPassword())){
           return  "Log in successfully";
       }else {
           throw  new InvalidCredentialException("Invalid credential");
       }
    }
}
