package com.testportal.online_test_portal.service.implementation;

import com.testportal.online_test_portal.dto.*;
import com.testportal.online_test_portal.entity.User;
import com.testportal.online_test_portal.enums.Role;
import com.testportal.online_test_portal.exception.custom.DuplicateEntryException;
import com.testportal.online_test_portal.exception.custom.InvalidCredentialException;
import com.testportal.online_test_portal.exception.custom.UserNotFoundException;
import com.testportal.online_test_portal.mapper.UserMapper;
import com.testportal.online_test_portal.repository.UserRepository;
import com.testportal.online_test_portal.service.UserService;
import jakarta.transaction.Transactional;
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
    public UserResponseDto registerUser(UserRegisterDto userRequestDto){
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
    public UserResponseDto registerAdmin(UserRegisterDto userRequestDto){
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


    @Override
    public UserProfileDto getUserById(Long id){
        Optional<User> userOptional = userRepository.findById(id);
        User user =
                userOptional.orElseThrow(()-> new UserNotFoundException(String.format("No user found with id: %d",id)));
        return UserProfileDto.builder()
                .id(user.getId())
                .name(user.getFirstName()+" "+user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedDate())
                .build();
    }

    @Transactional
    @Override
    public UserProfileDto updateUserProfile(UserUpdateDto dto){
        Optional<User> optionalUser = userRepository.findById(dto.getId());
        User user = optionalUser.orElseThrow(()-> new UserNotFoundException("No user found with this id"));

        // updating user fields from mapper
        UserMapper.updateUserFromDto(dto,user);

        User savedUser = userRepository.save(user);

        return UserProfileDto.builder()
                .id(savedUser.getId())
                .name(savedUser.getFirstName()+" "+savedUser.getLastName())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .createdAt(savedUser.getCreatedDate())
                .build();

    }
    @Override
    public void deleteUserById(Long id){
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException(String.format("No user found with id: %d",id));
        }
        userRepository.deleteById(id);
    }
    @Override
    public void updatePassword(PasswordUpdateDto passwordUpdateDto){
         Optional<User> optionalUser = userRepository.findByUsername(passwordUpdateDto.getUsername());
         User user = optionalUser.orElseThrow(()-> new UserNotFoundException("No user exist with this username"));

         if(!(passwordEncoder.matches(passwordUpdateDto.getOldPassword(),user.getPassword()))){
             throw new InvalidCredentialException("Please enter valid password");
         }

         String encodedNewPassword = passwordEncoder.encode(passwordUpdateDto.getNewPassword());
         user.setPassword(encodedNewPassword);
         userRepository.save(user);
    }
}
