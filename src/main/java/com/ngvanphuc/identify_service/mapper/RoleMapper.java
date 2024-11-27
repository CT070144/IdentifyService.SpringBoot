package com.ngvanphuc.identify_service.mapper;

import com.ngvanphuc.identify_service.dto.request.RoleRequest;
import com.ngvanphuc.identify_service.dto.response.RoleResponse;
import com.ngvanphuc.identify_service.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target="permissions", ignore=true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
