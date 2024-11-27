package com.ngvanphuc.identify_service.service;

import com.ngvanphuc.identify_service.dto.request.UserCreationRequest;
import com.ngvanphuc.identify_service.dto.request.UserUpdateRequest;
import com.ngvanphuc.identify_service.dto.response.UserResponse;
import com.ngvanphuc.identify_service.enums.Roles;
import com.ngvanphuc.identify_service.models.User;
import com.ngvanphuc.identify_service.exception.AppException;
import com.ngvanphuc.identify_service.exception.ErrorCode;
import com.ngvanphuc.identify_service.mapper.UserMapper;
import com.ngvanphuc.identify_service.repository.RoleRepository;
import com.ngvanphuc.identify_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor //Tự động tạo constructor cho lớp dựa trên các trường final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request){

          if(userRepository.existsByUsername(request.getUsername()))
              throw new AppException(ErrorCode.USER_EXISTED);

          User user = userMapper.toUser(request);
          user.setPassword(passwordEncoder.encode(request.getPassword()));
//          HashSet<String> roles = new HashSet<>();
//          roles.add(Roles.USER.name());
//          user.setRoles(roles);

         // user = userRepository.save(user);
          return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    //hasRole với scope có prefix ROLE_
    //hasAuthority với scope thông thường
    // Check condition before call method
    public List<UserResponse> getUsers(){
        log.info("In method get user list ");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }
     @PostAuthorize("returnObject.username==authentication.name")
    // Check condition after call method
    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }
    public UserResponse getInfo(){
       var context = SecurityContextHolder.getContext();
       String name = context.getAuthentication().getName();
       User user = userRepository.findByUsername(name).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXIT));
      return userMapper.toUserResponse(user);
    }
    public UserResponse updateUser(String userId,UserUpdateRequest request){
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(user,request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles()); //Trả về một list role
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }
}
