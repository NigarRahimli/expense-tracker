package com.codewithniki.expensetracker.mapper;

import com.codewithniki.expensetracker.model.dtos.user.AdminUserResponse;
import com.codewithniki.expensetracker.model.dtos.user.UpdateUserRequest;
import com.codewithniki.expensetracker.model.dtos.user.UserResponse;
import com.codewithniki.expensetracker.model.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // ================= BASIC USER RESPONSE =================

    UserResponse toResponse(User user);

    // ================= ADMIN RESPONSE =================

    @Mapping(target = "roles", expression = "java(user.getRoles().stream().map(r -> r.getName()).toList())")
    AdminUserResponse toAdminResponse(User user);

    // ================= UPDATE (PATCH STYLE) =================

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateUserRequest request, @MappingTarget User user);
}