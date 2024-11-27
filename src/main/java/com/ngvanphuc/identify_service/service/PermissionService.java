package com.ngvanphuc.identify_service.service;

import com.ngvanphuc.identify_service.dto.request.PermissionRequest;
import com.ngvanphuc.identify_service.dto.response.PermissionResponse;
import com.ngvanphuc.identify_service.mapper.PermissionMapper;
import com.ngvanphuc.identify_service.models.Permission;
import com.ngvanphuc.identify_service.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor //Tự động tạo constructor cho lớp dựa trên các trường final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {

        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        var permission = permissionRepository.findAll();
       return permission.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permission){
        permissionRepository.deleteById(permission);
    }
}
