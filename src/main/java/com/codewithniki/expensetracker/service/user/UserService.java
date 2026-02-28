package com.codewithniki.expensetracker.service.user;

import com.codewithniki.expensetracker.mapper.UserMapper;
import com.codewithniki.expensetracker.model.dtos.user.UpdateUserRequest;
import com.codewithniki.expensetracker.model.dtos.user.UserResponse;
import com.codewithniki.expensetracker.model.entities.User;
import com.codewithniki.expensetracker.exceptions.GlobalAppException;
import com.codewithniki.expensetracker.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse getUserById(Long id) {
        return userMapper.toResponse(findUser(id));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = findUser(id);

        if (request.getEmail() != null &&
                userRepository.existsByEmail(request.getEmail()) &&
                !request.getEmail().equals(user.getEmail())) {
            throw new GlobalAppException("Email already in use");
        }

        userMapper.updateEntity(request, user);

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(findUser(id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new GlobalAppException("User not found"));
    }
}