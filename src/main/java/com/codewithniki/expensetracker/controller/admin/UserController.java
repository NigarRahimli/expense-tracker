package com.codewithniki.expensetracker.controller.admin;

import com.codewithniki.expensetracker.model.dtos.user.UpdateUserRequest;
import com.codewithniki.expensetracker.model.dtos.user.UserResponse;
import com.codewithniki.expensetracker.service.user.IUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserResponse update(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request
    ) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}