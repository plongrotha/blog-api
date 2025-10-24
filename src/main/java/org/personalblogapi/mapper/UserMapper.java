package org.personalblogapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.personalblogapi.dto.RegisterRequest;
import org.personalblogapi.model.entity.User;
import org.personalblogapi.model.response.LoginResponse;
import org.personalblogapi.model.response.RegisterResponse;
import org.personalblogapi.model.response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "userId")
    UserResponse toUserResponse(User user);

    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "isRegistered", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toUser(RegisterRequest registerRequest);

    RegisterResponse toResponse(User user);

    @Mapping(target = "id", source = "userId")
    LoginResponse toResponseLogin(User user);
}
