package com.ngvanphuc.identify_service.mapper;

import com.ngvanphuc.identify_service.dto.request.UserUpdateRequest;
import com.ngvanphuc.identify_service.dto.response.UserResponse;
import com.ngvanphuc.identify_service.models.User;
import com.ngvanphuc.identify_service.dto.request.UserCreationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    void updateUser(@MappingTarget  User user, UserUpdateRequest request);
    UserResponse toUserResponse(User user);
}
