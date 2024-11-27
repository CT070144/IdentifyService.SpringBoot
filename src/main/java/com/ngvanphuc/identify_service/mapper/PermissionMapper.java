package com.ngvanphuc.identify_service.mapper;

import com.ngvanphuc.identify_service.dto.request.PermissionRequest;
import com.ngvanphuc.identify_service.dto.response.PermissionResponse;
import com.ngvanphuc.identify_service.dto.request.UserCreationRequest;
import com.ngvanphuc.identify_service.dto.request.UserUpdateRequest;
import com.ngvanphuc.identify_service.dto.response.UserResponse;
import com.ngvanphuc.identify_service.models.Permission;
import com.ngvanphuc.identify_service.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
