package com.codewithniki.expensetracker.service.user;

import com.codewithniki.expensetracker.model.dtos.user.UpdateUserRequest;
import com.codewithniki.expensetracker.model.dtos.user.UserResponse;

import java.util.List;

public interface IUserService {

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id);
}
