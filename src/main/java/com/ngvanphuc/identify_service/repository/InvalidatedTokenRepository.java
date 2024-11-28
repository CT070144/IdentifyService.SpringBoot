package com.ngvanphuc.identify_service.repository;

import com.ngvanphuc.identify_service.models.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken,String> {

}
