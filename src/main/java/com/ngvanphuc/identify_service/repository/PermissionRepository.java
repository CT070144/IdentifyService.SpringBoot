package com.ngvanphuc.identify_service.repository;

import com.ngvanphuc.identify_service.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

}
