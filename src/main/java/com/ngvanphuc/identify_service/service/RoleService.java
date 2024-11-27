package com.ngvanphuc.identify_service.service;

import com.ngvanphuc.identify_service.dto.request.RoleRequest;
import com.ngvanphuc.identify_service.dto.response.RoleResponse;
import com.ngvanphuc.identify_service.mapper.PermissionMapper;
import com.ngvanphuc.identify_service.mapper.RoleMapper;
import com.ngvanphuc.identify_service.models.Permission;
import com.ngvanphuc.identify_service.repository.PermissionRepository;
import com.ngvanphuc.identify_service.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor //Tự động tạo constructor cho lớp dựa trên các trường final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;
    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll(){
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }
    public void delete(String role){
        roleRepository.deleteById(role);
    }

}
